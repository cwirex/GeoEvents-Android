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
            TODO("Change inv status: on User.invitations & Event.participants")
            TODO("Add event to user: on User.events")
            TODO("Update events list: fetch accepted Event")
        }

        fun reject() {
            TODO("Change inv status: on User.invitations & Event.participants")
        }

        override fun getStatus(): Status {
            return Status.PENDING
        }
    }

    class Accepted(eid: String) : Invitation(eid) {
        fun resign() {
            TODO("Change inv status: on User.invitations & Event.participants")
            TODO("Remove event from user: on User.events")
            TODO("Update events list: remove Event")
        }

        override fun getStatus(): Status {
            return Status.ACCEPTED
        }
    }

    class Rejected(eid: String) : Invitation(eid) {
        fun accept() {
            TODO("Same as Pending.accept()")
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
