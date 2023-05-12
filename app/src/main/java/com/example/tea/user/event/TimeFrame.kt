package com.example.tea.user.event

import java.util.Date

class TimeFrame(val start: Date, val end: Date) {
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