package com.example.tea.user.status

import com.example.tea.user.model.Marker
import java.time.LocalDateTime
import java.util.Date

data class Status(
    val time: LocalDateTime,
    val position: Marker
)
