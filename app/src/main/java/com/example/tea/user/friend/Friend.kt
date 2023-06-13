package com.example.tea.user.friend

import com.example.tea.user.status.Status

data class Friend(val id: String, val nickname: String = "", val status: Status? = null)
//TODO: friend's lastPosition ?
