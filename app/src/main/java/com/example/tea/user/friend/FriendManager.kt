package com.example.tea.user.friend

import android.util.Log
import com.example.tea.user.User
import com.example.tea.map.Marker
import com.example.tea.user.status.Status
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

/** Holds user's friends and manages them */
class FriendManager(val user: User) {
    private val friends: HashMap<String, Friend> = hashMapOf()
    private val emailToUidMap: HashMap<String, String> = hashMapOf()

    companion object {
        const val TAG = "FriendManager"
    }

    init {
        fetchEmailToUidMap { }
        fetchFriends { }
    }

    private fun fetchEmailToUidMap(callback: (Boolean) -> Unit) {
        Firebase.firestore.collection("info")
            .document("emailToUid")
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val data = doc.data as? Map<String, String>
                    emailToUidMap.putAll(data ?: emptyMap())
                    Log.d(TAG, emailToUidMap.toString())        //TODO debug
                    callback(true)
                } else callback(false)
            }
            .addOnSuccessListener { callback(false) }
    }

    private fun fetchFriends(callback: (Map<String, Friend>) -> Unit) {
        user.userManager.getUserData { it ->
            it?.let {
                val keys = it.friends.keys.toList()
                var keysLeft = keys.size
                keys.forEach { fid ->
                    fetchFriend(fid) { friend ->
                        if (friend != null)
                            friends[fid] = friend
                        keysLeft -= 1
                        if (keysLeft <= 0) callback(friends.toMap())
                    }
                }
            }
        }
    }

    fun getUserFriends(callback: (Map<String, Friend>) -> Unit) {
        if (friends.isNotEmpty())
            callback(friends.toMap())
        else {
            fetchFriends(callback)
        }
    }

    fun updateFriends(friendsInfo: Map<String, String>) {
        friendsInfo.forEach { (fid, _) ->
            friends[fid] = Friend(fid)
            user.userManager.getUserData(fid) {
                if (it != null) {
                    friends[fid] = Friend(
                        id = fid,
                        nickname = it.nick,
                        status= Status(
                            LocalDateTime.parse(it.lastSeen),
                            Marker(
                                lat = it.lastLat,
                                lon = it.lastLong
                            )
                        )
                    )
                }
            }
        }
    }

    fun getIdByEmail(email: String, callback: (String?) -> Unit) {
        if (!emailToUidMap.containsKey(email)) {  // if there is no such key, try to update map
            fetchEmailToUidMap {
                if (it) {
                    callback(emailToUidMap[email])
                } else {
                    Log.w(TAG, "email not found at getIdByEmail")
                    callback(null)
                }
            }
        } else callback(emailToUidMap[email])     // just return fid
    }

    /** Adds friend on DB and returns new Friend's info */
    fun addFriend(fid: String, callback: (Friend?) -> Unit) {
        // add friend to db (& locally when fetched)
        val users = Firebase.firestore.collection("users")
        users.document(user.getId())
            .update("friends.${fid}", fid)
            .addOnSuccessListener {
                users.document(fid)
                    .update("friends.${user.getId()}", user.getId())
                    .addOnSuccessListener { // success, link between friends established
                        fetchFriend(fid, callback)
                    }
                    .addOnFailureListener {
                        callback(null)
                        Log.e(
                            TAG,
                            "Error adding new friend $fid #onFriend (doc might not exist)",
                            it
                        )
                    }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error adding new friend $fid #onUser (doc might not exist)", it)
                callback(null)
            }
    }

    private fun fetchFriend(fid: String, callback: (Friend?) -> Unit) {
        user.userManager.getUserData(fid) {
            if (it != null) {
                val time = LocalDateTime.parse(it.lastSeen)
                val marker = Marker(it.lastLat, it.lastLong)
                val status = Status(time, marker)
                val friend = Friend(fid, status = status)
                callback(friend)
            } else callback(null)
        }
    }
}