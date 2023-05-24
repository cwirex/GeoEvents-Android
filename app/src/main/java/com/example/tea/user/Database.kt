package com.example.tea.user

import com.example.tea.user.event.Event

interface Database {
    interface Users {
        data class UserData(
            val uid: String,
            val nick: String,
            val lastSeen: String,
            val lastLong: Double,
            val lastLat: Double,
            val events: Map<String, Boolean>,
            val invitations: Map<String, Int>,
            val friends: Map<String, String>
        ) {
            constructor() : this("", "", "", 0.0, 0.0, emptyMap(), emptyMap(), emptyMap() ) // required by Firebase
        }
        fun getUserData(callback: (user: UserData?) -> Unit)
        fun addUser(user: UserData)
        fun updateUser(updateMap: Map<String, Any>)
        fun deleteUser()
    }

    interface Events {
        data class EventData(
            val eid: String = "",
            val owner: String = "",
            val title: String = "",
            val desc: String = "",
            val timeStart: String = "",
            val timeEnd: String = "",
            val locationName: String = "",
            val long: Double = 0.0,
            val lat: Double = 0.0,
            val participants: Map<String, Int> = emptyMap()
        ){
            constructor() : this("","", "", "", "", "", "", 0.0, 0.0, emptyMap())   // required by Firebase
        }

        fun getEvent(eid: String, callback: (event: Event?) -> Unit)
        fun addEvent(eid: String, event: EventData)
        fun updateEvent(eid: String, updateMap: Map<String, Any>)
        fun deleteEvent(eid: String)
    }
}
