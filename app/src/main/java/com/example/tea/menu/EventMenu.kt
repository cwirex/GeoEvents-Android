package com.example.tea.menu

import com.example.tea.map.Marker

data class EventMenu(
    var title: String = "",
    var marker: Marker = Marker(0.0,0.0),
)