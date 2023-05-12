package com.example.tea.user.invitation

import com.example.tea.user.event.Event

sealed class Invitation(
    val id: String,
    val event: Event? = null    // TODO: czy nie lepiej przechowywać EventID?
){
    class Pending(id: String) :Invitation(id){
        fun accept(){
            //
        }

        fun reject(){
            //
        }
    }

    class Accepted(id: String) :Invitation(id){
        fun resign(){
            //
        }
    }

    class Rejected(id: String) :Invitation(id){
        fun accept(){
            //
        }
    }

    class Expired(id: String) :Invitation(id){
        //
    }
}
