package com.example.tea.user.event

import java.time.LocalDateTime

class TimeFrame(val start: LocalDateTime, val end: LocalDateTime) {
    /** Returns value in minutes */
    fun getDuration(): Int {
        return 0
    }

    /** Returns value in a given format
     * m -> minutes,
     * */
    fun getTimeToStart(format: String = "m"): Int {
        return 0
    }

}