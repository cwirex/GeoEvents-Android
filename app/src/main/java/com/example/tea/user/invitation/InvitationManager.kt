package com.example.tea.user.invitation

import com.example.tea.user.User

/** Holds user's invitations and manages them */
class InvitationManager(val user: User) {
    private val invitations: MutableMap<String, Invitation> = mutableMapOf()

    //TODO: fetch, send to DB

    fun getPendingInvitations(): HashMap<String, Invitation.Pending>{
        val pending = HashMap<String, Invitation.Pending>()
        invitations.forEach { (key, value) ->
            if (value is Invitation.Pending) {
                pending[key] = value
            }
        }
        return pending
    }

    fun getAcceptedInvitations(): HashMap<String, Invitation.Accepted> {
        val accepted = HashMap<String, Invitation.Accepted>()
        invitations.forEach { (key, value) ->
            if (value is Invitation.Accepted) {
                accepted[key] = value
            }
        }
        return accepted
    }

    fun getEventInvitations(): HashMap<String, Invitation> {
        return HashMap(invitations)
    }
}
