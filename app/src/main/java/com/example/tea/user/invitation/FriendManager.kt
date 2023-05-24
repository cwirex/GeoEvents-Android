package com.example.tea.user.invitation

import com.example.tea.user.User

/** Holds user's friends and manages them */
class FriendManager(val user: User) {

    //TODO fetch friends list, fetch nicknames
    //TODO add friends

    private val friends: MutableMap<String, Friend> = mutableMapOf()
    fun getUserFriends(): HashMap<String, Friend> {
        return HashMap(friends)
    }
}