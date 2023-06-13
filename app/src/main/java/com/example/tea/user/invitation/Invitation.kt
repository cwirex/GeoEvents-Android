package com.example.tea.user.invitation

import com.example.tea.user.User
import com.example.tea.user.event.Event

sealed class Invitation(
    val eid: String,
) {
    enum class Status { PENDING, ACCEPTED, REJECTED, EXPIRED }

    companion object {
        lateinit var user: User
    }

    abstract fun getStatus(): Status
    fun getLinkedEvent(callback: (Event?) -> Unit) {
        user.eventManager.getEvent(eid) {
            callback(it)
        }
    }

    class Pending(eid: String) : Invitation(eid) {
        fun accept() {
            user.invitationManager.sendInvitation(eid, user.getId(), Status.ACCEPTED)
            user.eventManager.notifyEventStatusChanged(eid, Status.ACCEPTED)
        }

        fun reject() {
            user.invitationManager.sendInvitation(eid, user.getId(), Status.REJECTED)
            user.eventManager.notifyEventStatusChanged(eid, Status.REJECTED)
            //TODO: ? Remove event from DB.users.$uid.events ?
        }

        override fun getStatus(): Status {
            return Status.PENDING
        }
    }

    class Accepted(eid: String) : Invitation(eid) {
        fun resign() {
            user.invitationManager.sendInvitation(eid, user.getId(), Status.REJECTED)
            user.eventManager.notifyEventStatusChanged(eid, Status.REJECTED)
            //TODO: ? Remove event from DB.users.$uid.events ?
        }

        override fun getStatus(): Status {
            return Status.ACCEPTED
        }
    }

    class Rejected(eid: String) : Invitation(eid) {
        fun accept() {
            user.invitationManager.sendInvitation(eid, user.getId(), Status.ACCEPTED)
            user.eventManager.notifyEventStatusChanged(eid, Status.ACCEPTED)
        }

        override fun getStatus(): Status {
            return Status.REJECTED
        }
    }

    class Expired(eid: String) : Invitation(eid) {
        override fun getStatus(): Status {
            return Status.EXPIRED
        }
    }
}
