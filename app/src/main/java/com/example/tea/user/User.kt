package com.example.tea.user

/** Class responsible for providing info and all user actions (served by UserManager) */
class User(private val id: String) {
    lateinit var nickname: String
    private val userManager = UserManager(this)


}