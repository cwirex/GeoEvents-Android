package com.example.tea.user

import com.example.tea.user.event.EventManager

/** Class responsible for providing info and all user actions (served by UserManager) */
class User(private val id: String) {
    lateinit var nickname: String
    val userManager = UserManager(this)
    val eventManager = EventManager(this)

    fun getId(): String {return this.id}
}