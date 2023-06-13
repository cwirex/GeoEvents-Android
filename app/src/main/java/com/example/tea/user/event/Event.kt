package com.example.tea.user.event

import com.example.tea.user.invitation.Invitation
import com.example.tea.map.Marker
import java.time.LocalDateTime

data class Event(
    var eid: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val timeFrame: TimeFrame,
    val location: Location,
    val participants: MutableMap<String, Participant>,
) {
    constructor() : this(
        "", // Provide default values for the properties
        "",
        "",
        "",
        TimeFrame(LocalDateTime.MIN, LocalDateTime.MIN),
        Location("", Marker(0.0, 0.0)),
        mutableMapOf()
    )

    data class Participant(
        val uid: String,
        val nickname: String,
        var status: Invitation.Status
    )
}

