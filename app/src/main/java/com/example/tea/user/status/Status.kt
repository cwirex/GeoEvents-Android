package com.example.tea.user.status

import com.example.tea.map.Marker
import java.time.LocalDateTime

data class Status(
    val time: LocalDateTime,
    val position: Marker
)
