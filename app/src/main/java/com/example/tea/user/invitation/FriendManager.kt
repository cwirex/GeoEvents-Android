package com.example.tea.user.invitation

import com.example.tea.user.User
import com.example.tea.user.model.Marker
import com.example.tea.user.status.Status
import java.time.LocalDateTime

/** Holds user's friends and manages them */
class FriendManager(val user: User) {
    private val friends: MutableMap<String, Friend> = mutableMapOf()


    fun getUserFriends(): HashMap<String, Friend> {
        return HashMap(friends)
    }

    fun updateFriends(friendsInfo: Map<String, String>) {
        friendsInfo.forEach{(fid, _) ->
            friends[fid] = Friend(fid)
            user.userManager.getUserData(fid){
                if(it != null){
                    friends[fid] = Friend(
                        id=fid,
                        nickname=it.nick,
                        status= Status(
                            LocalDateTime.parse(it.lastSeen),
                            Marker(
                                lat=it.lastLat,
                                long=it.lastLong)
                        )
                    )
                }
            }
        }
    }

    fun addFriend(friend: Friend) {
        friends[friend.id] = friend
        //TODO send friend to DB, fetch nickname and last status
    }
}