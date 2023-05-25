package com.example.tea.user.invitation

import com.example.tea.user.User

/** Holds user's invitations and manages them */
class InvitationManager(val user: User) {
    private val invitations: MutableMap<String, Invitation> = mutableMapOf()

    //TODO Create new && Send invitations to db

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

    fun updateInvitations(invitationsInfo: Map<String, Int>) {
        invitationsInfo.forEach{(iid, statusCode) ->
            var inv: Invitation? = null
            when(statusCode){
                Invitation.Status.Pending.ordinal -> {
                    inv = Invitation.Pending(iid)
                }
                Invitation.Status.Accepted.ordinal -> {
                    inv = Invitation.Accepted(iid)
                }
                Invitation.Status.Expired.ordinal -> {
                    inv = Invitation.Expired(iid)
                }
                Invitation.Status.Rejected.ordinal -> {
                    inv = Invitation.Rejected(iid)
                }
            }
            if(inv != null) {
                invitations[iid] = inv
                user.eventManager.getEvent(iid){
                    if(it != null){
                        invitations[iid]?.event = it
                    }
                }
            }
        }
    }
}
