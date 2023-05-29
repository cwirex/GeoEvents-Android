package com.example.tea.user.status

import com.example.tea.user.User
import com.example.tea.user.model.Marker
import java.time.LocalDateTime

/** Holds user's statuses and manages them */
class StatusManager(val user: User) {
    private val statuses: MutableList<Status> = mutableListOf()

    //todo: monitor position (look at time from last insertion and changes)
    //todo: update db when required

    fun addPosition(marker: Marker){
        statuses.add(Status(LocalDateTime.now(), marker))
    }

    fun getLastPosition(): Marker? {
        return statuses.lastOrNull()?.position
    }

    fun getLastStatus(): Status? {
        return statuses.lastOrNull()
    }

    fun updateStatus(status: Status) {
        statuses.add(status)
        //TODO update DB when time elapsed (or position changed)
    }
}
