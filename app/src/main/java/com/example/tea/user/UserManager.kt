package com.example.tea.user

import com.example.tea.user.event.EventManager
import com.example.tea.user.invitation.FriendManager
import com.example.tea.user.invitation.InvitationManager
import com.example.tea.user.status.StatusManager
import com.google.firebase.firestore.FirebaseFirestore

/** Provides interface for User with simplified actions */
class UserManager internal constructor(val user: User) : Database.Users{
    private val userRef = FirebaseFirestore.getInstance().collection("users").document(user.getId())

    override fun getUserData(callback: (user: Database.Users.UserData?) -> Unit) {
        userRef.get()
            .addOnSuccessListener { doc ->
                if (doc.exists())
                    callback(doc.toObject(Database.Users.UserData::class.java))
            }
            .addOnFailureListener{ callback(null)}
    }

    override fun addUser(user: Database.Users.UserData) {
        userRef.set(user)
            .addOnFailureListener{e ->
                throw Exception("Exception on addUser ${user.uid}: $e")
            }
    }

    override fun updateUser(updateMap: Map<String, Any>) {
        val isPojo = !updateMap.values.any { value ->
            value is Map<*, *> || value is List<*>
        }
        if(isPojo) {
            userRef.update(updateMap)
                .addOnFailureListener{ e ->
                    throw Exception("Failed to update document ${user.getId()}: $e") }
        }
    }

    override fun deleteUser() {
        userRef.delete()
            .addOnFailureListener {
                    e -> throw Exception("Failed to delete user ${user.getId()}: $e") }
    }

}