package com.example.tea.user

import com.example.tea.user.event.Event
import com.example.tea.user.invitation.Invitation
import com.example.tea.map.Marker
import com.example.tea.user.status.Status
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

/** Provides interface for User with simplified actions */
class UserManager internal constructor(val user: User) : Database.Users {
    private val collectionRef = FirebaseFirestore.getInstance().collection("users")
    private val userRef = collectionRef.document(user.getId())

    /** Retrieves userData from DB in a callback*/
    override fun getUserData(uid: String?, callback: (user: Database.Users.UserData?) -> Unit) {
        collectionRef.document(uid ?: user.getId())
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists())
                    callback(doc.toObject(Database.Users.UserData::class.java))
                else
                    callback(null)
            }
            .addOnFailureListener { callback(null) }
    }


    /** Sends user to DB */
    override fun addUser(user: Database.Users.UserData) {
        userRef.set(user)
            .addOnFailureListener { e ->
                throw Exception("Exception on addUser ${user.uid}: $e")
            }
    }

    /** Sends this.user to DB */
    fun addUser() {
        userToPojo {
            userRef.set(it)
                .addOnFailureListener { e ->
                    throw Exception("Exception on addUser ${user.getId()}: $e")
                }
        }
    }

    /** Updates DB user fields (with update map)*/
    override fun updateUser(updateMap: Map<String, Any>) {
        updateUser(user.getId(), updateMap)
    }

    /** Deletes user from DB */
    override fun deleteUser() {
        userRef.delete()
            .addOnFailureListener { e -> throw Exception("Failed to delete user ${user.getId()}: $e") }
    }

    fun updateUser(uid: String, updateMap: Map<String, Any>) {
        val isPojo = !updateMap.values.any { value ->
            value is Map<*, *> || value is List<*>
        }
        if (isPojo) {
            collectionRef.document(uid).update(updateMap)
                .addOnFailureListener { e ->
                    throw Exception("Failed to update document ${user.getId()}: $e")
                }
        }
    }

    /** Fills User fields with sample data */
    fun makeSampleUser() {
        user.friendManager.addFriend("friend1-id") {}
        user.friendManager.addFriend("friend2-id") {}
        user.eventManager.addEvent(user.eventManager.getSampleEvent())
        user.statusManager.updateStatus(Status(LocalDateTime.now(), Marker(0.0, 0.0)))
        user.invitationManager.updateInvitations(
            mapOf(
                "userInv1" to 0,
                "userInv2" to 2,
                "userInv3" to 1
            )
        )
        user.nickname = "sample-nickname"
    }

    /** Map User to POJO*/
    private fun userToPojo(callback: (Database.Users.UserData) -> Unit) {
        val uid = user.getId()
        val nick = user.nickname
        val lastSeen = LocalDateTime.now().toString()
        val lastPosition = user.getPosition()
        val lastLong = lastPosition?.lon ?: 0.0
        val lastLat = lastPosition?.lat ?: 0.0


        val eventsFuture = CompletableFuture.supplyAsync {
            val future = CompletableFuture<Map<String, Boolean>>()
            user.getEvents { eventMap: Map<String, Event>? ->
                val events: Map<String, Boolean> =
                    eventMap?.mapValues { entry -> entry.value.ownerId == uid } ?: emptyMap()
                future.complete(events)
            }
            future.get()
        }

        val invitationsFuture = CompletableFuture.supplyAsync {
            val future = CompletableFuture<Map<String, Int>>()
            user.getInvitations { invitationMap: Map<String, Invitation>? ->
                val invitations: Map<String, Int> =
                    invitationMap?.mapValues { entry -> entry.value.getStatus().ordinal }
                        ?: emptyMap()
                future.complete(invitations)
            }
            future.get()
        }

        val friendsFuture = CompletableFuture.supplyAsync {
            val future = CompletableFuture<Map<String, String>>()
            user.getFriends { friends ->
                val mappedFriends: Map<String, String> =
                    friends?.mapValues { entry -> entry.key } ?: emptyMap()
                future.complete(mappedFriends)
            }
            future.get()
        }

        CompletableFuture.allOf(eventsFuture, invitationsFuture, friendsFuture).join()

        val userData = Database.Users.UserData(
            uid = uid,
            nick = nick,
            lastSeen = lastSeen,
            lastLong = lastLong,
            lastLat = lastLat,
            events = eventsFuture.get(),
            invitations = invitationsFuture.get(),
            friends = friendsFuture.get()
        )

        callback(userData)
    }

    /** Fetches user data and updates User fields*/
    fun fetchAndUpdateUser() {
        getUserData { userData: Database.Users.UserData? ->
            if (userData != null) {
                updateUserFields(userData)
            }
        }
    }

    /** Updates User based with fetched userData*/
    fun updateUserFields(userData: Database.Users.UserData) {
        user.nickname = userData.nick
        user.eventManager.setEventsInfo(userData.events)
        user.friendManager.updateFriends(userData.friends)
        user.invitationManager.updateInvitations(userData.invitations)
    }

    fun createDbUser(callback: (Boolean) -> Unit, id: String? = null) {
        if (id == null) {
            val userData = Database.Users.UserData(
                uid = user.getId(),
                nick = user.nickname,
                lastSeen = LocalDateTime.now().toString(),
                lastLong = 0.0,
                lastLat = 0.0,
                events = mapOf(),
                invitations = mapOf(),
                friends = mapOf()
            )

            userRef
                .set(userData)
                .addOnSuccessListener { callback(true) }
                .addOnFailureListener { callback(false) }
        }

    }
}