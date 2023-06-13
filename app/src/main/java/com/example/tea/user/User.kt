package com.example.tea.user

import com.example.tea.user.event.Event
import com.example.tea.user.event.EventManager
import com.example.tea.user.friend.Friend
import com.example.tea.user.friend.FriendManager
import com.example.tea.user.invitation.Invitation
import com.example.tea.user.invitation.InvitationManager
import com.example.tea.map.Marker
import com.example.tea.user.status.StatusManager

/** Class responsible for providing info and all user actions (served by UserManager) */
class User(private val id: String) {
    var nickname: String = ""
    val userManager = UserManager(this)
    val invitationManager = InvitationManager(this)
    val eventManager = EventManager(this)
    val statusManager = StatusManager(this)
    val friendManager = FriendManager(this)


    // TODO define usefull functions (other can be reached directly from managers)
    fun getId(): String {return this.id}
    fun getEvents(callback: (Map<String, Event>?) -> Unit){
        this.eventManager.getUserEvents(callback)
    }

    fun getPosition(): Marker? {
        return this.statusManager.getLastPosition()
    }

    fun getInvitations(callback: (Map<String, Invitation>?) -> Unit) {
        this.invitationManager.getEventInvitations(callback)
    }

    fun getFriends(callback: (Map<String, Friend>?) -> Unit) {
        friendManager.getUserFriends(callback)
    }
}