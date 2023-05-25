package com.example.tea.user.invitation

import com.example.tea.user.event.Event

sealed class Invitation(
    val id: String,
    var event: Event? = null    // TODO: czy nie lepiej przechowywać EventID?
){
    enum class Status{Pending, Accepted, Rejected, Expired}
    abstract fun getStatus(): Status
    class Pending(id: String) :Invitation(id){
        fun accept(){
            //
        }

        fun reject(){
            //
        }

        override fun getStatus(): Status {
            return Status.Pending
        }
    }

    class Accepted(id: String) :Invitation(id){
        fun resign(){
            //
        }

        override fun getStatus(): Status {
            return Status.Accepted
        }
    }

    class Rejected(id: String) :Invitation(id){
        fun accept(){
            //
        }
        override fun getStatus(): Status {
            return Status.Rejected
        }
    }

    class Expired(id: String) :Invitation(id){
        override fun getStatus(): Status {
            return Status.Expired
        }
    }
}
