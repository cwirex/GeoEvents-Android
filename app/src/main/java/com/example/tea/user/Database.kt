package com.example.tea.user

import com.example.tea.user.event.Event

interface Database {
    interface Users {
        data class UserData(
            val nick: String,
            val lastSeen: String,   // timestamp
            val lastLong: Double,
            val lastLat: Double,
            val events: Map<String, Boolean>,
            val invitations: Map<String, Int>,
            val friends: Map<String, String>
        )

        fun getUser(uid: String): UserData?
        fun addUser(uid: String, user: UserData)
        fun updateUser(uid: String, user: UserData)
        fun deleteUser(uid: String)
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
            constructor() : this("","", "", "", "", "", "", 0.0, 0.0, emptyMap())
        }

        fun getEvent(eid: String, callback: (event: Event?) -> Unit)
        fun addEvent(eid: String, event: EventData)
        fun updateEvent(eid: String, event: EventData)
        fun deleteEvent(eid: String)
    }
}
