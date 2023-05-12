package com.example.tea.user.status

import com.example.tea.user.model.Marker
import java.util.Date

data class Status(
    val time: Date,
    val position: Marker
)
