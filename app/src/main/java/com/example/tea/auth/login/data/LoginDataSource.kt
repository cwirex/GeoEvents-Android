package com.example.tea.auth.login.data

import android.util.Log
import com.example.tea.auth.login.data.model.LoggedInUser
import com.example.tea.user.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    companion object {
        const val TAG = "LoginDataSource"
        fun getDisplayName(user: FirebaseUser?): String{
            return user?.email?.substringBefore("@").toString()
        }
    }

    /** Function logs in user based on Firebase credentials */
    private fun loginUser(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { signInTask ->
                if (signInTask.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    val uid = user!!.uid
                    val displayName = getDisplayName(user)  //TODO: FETCH NICKNAME from DB
                    linkEmailToUid(username, uid)
                    val loggedInUser = LoggedInUser(uid, displayName)
                    callback(Result.Success(loggedInUser))
                } else {
                    Log.w(TAG, "signInWithEmail:failure", signInTask.exception)
                    callback(Result.Error(IOException("Error logging in")))
                }
            }
    }

    /** Creates new user on Firebase (and logs in) */
    private fun registerUser(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { createTask ->
                if (createTask.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val uid = user!!.uid
                    val displayName = getDisplayName(user)  //TODO: ASK USER FOR NICKNAME (on just registered account)
                    linkEmailToUid(username, uid)           //Create new link email -> UID
                    User(uid).createDbAccount {  }     //Create user on DB
                    val loggedInUser = LoggedInUser(uid, displayName)
                    callback(Result.Success(loggedInUser))
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", createTask.exception)
                    callback(Result.Error(IOException("Error creating account")))
                }
            }
    }

    /** Registers or Logs in user based on credentials */
    fun signInUser(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        try {
            val auth = Firebase.auth
            auth.fetchSignInMethodsForEmail(username)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val signInMethods = task.result?.signInMethods ?: emptyList()
                        if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                            loginUser(username, password, callback)  // User already exists, log them in

                        } else {
                            registerUser(username, password, callback)  // User doesn't exist, create new account and log them in
                        }
                    } else {
                        Log.w(TAG, "fetchSignInMethodsForEmail:failure", task.exception)
                        callback(Result.Error(IOException("Error checking email")))
                    }
                }
        } catch (e: Throwable) {
            callback(Result.Error(IOException("Error on logging attempt", e)))
        }
    }

    private fun linkEmailToUid(email: String, uid: String) {
        Firebase.firestore
            .collection("info")
            .document("emailToUid")
            .set(mapOf(email to uid), SetOptions.merge())
            .addOnSuccessListener {
                //
            }
            .addOnFailureListener{
                Log.w(TAG, "linkEmailToUid failure: ", it)
            }
    }

    fun logout() {
        Firebase.auth.signOut()
    }
}