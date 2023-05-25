package com.example.tea.user

import com.example.tea.user.event.Event
import com.example.tea.user.event.EventManager
import com.example.tea.user.invitation.FriendManager
import com.example.tea.user.invitation.InvitationManager
import com.example.tea.user.status.StatusManager
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

/** Provides interface for User with simplified actions */
class UserManager internal constructor(val user: User) : Database.Users{
    private val collectionRef = FirebaseFirestore.getInstance().collection("users")
    private val userRef = collectionRef.document(user.getId())

    override fun getUserData(uid: String?, callback: (user: Database.Users.UserData?) -> Unit) {
            collectionRef.document(uid ?: user.getId())
            .get()
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

    fun userToPojo(): Database.Users.UserData{
        val uid = user.getId()
        val nick = user.nickname
        val lastSeen = LocalDateTime.now().toString()
        val lastPosition = user.getPosition()
        val lastLong = lastPosition?.long ?: 0.0
        val lastLat = lastPosition?.lat ?: 0.0
        val events: Map<String, Boolean> = user.getEvents()
            .mapValues {entry -> entry.value.ownerId == uid  }
        val invitations: Map<String, Int> = user.getInvitations()
            .mapValues { entry -> entry.value.getStatus().ordinal }
        val friends: Map<String, String> = user.getFriends()
            .mapValues { entry -> entry.key }   //todo: what to hold in here?

        return Database.Users.UserData(uid, nick, lastSeen, lastLong, lastLat, events, invitations, friends)
    }

    /** Updates User based with fetched userData*/
    fun updateUser(userData: Database.Users.UserData){
        user.nickname = userData.nick
        user.eventManager.updateEvents(userData.events)
        user.friendManager.updateFriends(userData.friends)  // TODO
        user.invitationManager.updateInvitations(userData.invitations)
//        TODO("Update User fields based on fetched userData")
    }
}