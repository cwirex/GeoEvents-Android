package com.example.tea.user.event

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val timeFrame: TimeFrame,
    val location: Location
)
