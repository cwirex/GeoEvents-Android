package com.example.tea.data

import android.util.Log
import com.example.tea.data.model.LoggedInUser
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
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

    fun login(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        try {
            val auth = Firebase.auth
            auth.fetchSignInMethodsForEmail(username)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val signInMethods = task.result?.signInMethods ?: emptyList()
                        if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                            // User already exists, log them in
                            auth.signInWithEmailAndPassword(username, password)
                                .addOnCompleteListener { signInTask ->
                                    if (signInTask.isSuccessful) {
                                        Log.d(TAG, "signInWithEmail:success")
                                        val user = auth.currentUser
                                        val displayName = getDisplayName(user)
                                        val loggedInUser = LoggedInUser(user!!.uid, displayName)
                                        callback(Result.Success(loggedInUser))
                                    } else {
                                        Log.w(TAG, "signInWithEmail:failure", signInTask.exception)
                                        callback(Result.Error(IOException("Error logging in")))
                                    }
                                }
                        } else {
                            // User doesn't exist, create new account and log them in
                            auth.createUserWithEmailAndPassword(username, password)
                                .addOnCompleteListener { createTask ->
                                    if (createTask.isSuccessful) {
                                        Log.d(TAG, "createUserWithEmail:success")
                                        val user = auth.currentUser
                                        val displayName = user?.email?.substringBefore("@")
                                        val loggedInUser = LoggedInUser(user!!.uid, displayName!!)
                                        callback(Result.Success(loggedInUser))
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", createTask.exception)
                                        callback(Result.Error(IOException("Error creating account")))
                                    }
                                }
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

    fun logout() {
        Firebase.auth.signOut()
    }
}