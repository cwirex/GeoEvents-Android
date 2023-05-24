package com.example.tea.user

import com.example.tea.user.event.Event
import com.example.tea.user.event.EventManager
import com.example.tea.user.invitation.Friend
import com.example.tea.user.invitation.FriendManager
import com.example.tea.user.invitation.Invitation
import com.example.tea.user.invitation.InvitationManager
import com.example.tea.user.model.Marker
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
    fun getEvents(): HashMap<String, Event>{
        return this.eventManager.getUserEvents()
    }
    fun getPosition(): Marker?{
        return this.statusManager.getLastPosition()
    }

    fun getInvitations(): HashMap<String, Invitation>{
        return this.invitationManager.getEventInvitations()
    }

    fun getFriends(): HashMap<String, Friend>{
        return this.friendManager.getUserFriends()
    }
}