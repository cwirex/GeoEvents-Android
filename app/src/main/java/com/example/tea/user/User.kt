package com.example.tea.user

import com.example.tea.user.event.EventManager
import com.example.tea.user.invitation.FriendManager
import com.example.tea.user.invitation.InvitationManager
import com.example.tea.user.status.StatusManager

/** Class responsible for providing info and all user actions (served by UserManager) */
class User(private val id: String) {
    var nickname: String = ""
    val userManager = UserManager(this)
    val invitationManager = InvitationManager(this)
    val eventManager = EventManager(this)
    val statusManager = StatusManager(this)
    val friendManager = FriendManager(this)


    fun getId(): String {return this.id}
}