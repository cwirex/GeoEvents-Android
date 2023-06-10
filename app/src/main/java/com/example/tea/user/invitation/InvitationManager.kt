package com.example.tea.user.invitation

import com.example.tea.user.Database
import com.example.tea.user.User
import com.example.tea.user.event.Event

/** Holds user's invitations and manages them */
class InvitationManager(val user: User) {
    private val invitations: MutableMap<String, Invitation> = mutableMapOf()

    init {
        Invitation.user = user
    }

    /** Generates invitations for event and sends them to DB (local list is also updated).
     * Works for NEW & UPDATED events.
     * Ignores old (non-existing by keys) invitations */
    fun makeInvitations(event: Event) {
        event.participants.forEach { (key, participant) ->
            val eid = event.eid
            sendInvitation(eid, participant.uid, participant.status)

            if (participant.uid == user.getId()) {
                invitations[eid] = Invitation.Expired(eid)
            }
        }
    }

    /** Sends invitation (creates link) on Event.participants and User.invitations */
    private fun sendInvitation(eid: String, uid: String, status: Invitation.Status) {
        user.eventManager.updateEvent(eid, mapOf("participants.${uid}" to status.ordinal))
        user.userManager.updateUser(uid, mapOf("invitations.$eid" to status.ordinal))
    }

    /** Creates desirable type of Invitation based on delivered Status*/
    private fun makeInvByStatus(eid: String, status: Invitation.Status): Invitation {
        val invitation: Invitation = when (status) {
            Invitation.Status.PENDING -> Invitation.Pending(eid)
            Invitation.Status.ACCEPTED -> Invitation.Accepted(eid)
            Invitation.Status.REJECTED -> Invitation.Rejected(eid)
            Invitation.Status.EXPIRED -> Invitation.Expired(eid)
        }
        return invitation
    }

    fun getPendingInvitations(): HashMap<String, Invitation.Pending> {
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

    /** Fetches invitations from DB and updates local list.
     *
     * If fetch=false -> return from local list */
    fun getEventInvitations(callback: (Map<String, Invitation>?) -> Unit, fetch: Boolean = true) {
        if (invitations.isNotEmpty() and !fetch) {
            callback(invitations.toMap())
        } else { // fetch
            user.userManager.getUserData { data: Database.Users.UserData? ->
                if (data != null) {
                    updateInvitations(data.invitations) // sync task
                    callback(invitations)
                } else {
                    callback(null)
                }
            }
        }
    }

    /** Updates local list of invitations */
    fun updateInvitations(invitationsInfo: Map<String, Int>) {
        invitationsInfo.forEach { (iid, statusCode) ->
            val status =
                Invitation.Status.values().getOrElse(statusCode) { Invitation.Status.PENDING }
            invitations[iid] = makeInvByStatus(iid, status)
        }
    }
}
