package com.example.tea.user

import com.example.tea.user.event.EventManager
import com.example.tea.user.invitation.FriendManager
import com.example.tea.user.invitation.InvitationManager
import com.example.tea.user.status.StatusManager

/** Provides interface for User with simplified actions */
class UserManager internal constructor(val user: User) {
    private val invitationManager = InvitationManager(user)
    private val eventManager = EventManager(user)
    private val statusManager = StatusManager(user)
    private val friendManager = FriendManager(user)

    // acceptInvitation(), createEvent(), listFriends()...
}