package com.example.tea.user.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.R
import com.example.tea.adapters.FriendAdapter

class FriendsListActivity : AppCompatActivity() {

    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var  friendArrayList: ArrayList<Friend>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_list)
        friendRecyclerView = findViewById(R.id.friendsRecyclerView)
        friendRecyclerView.layoutManager = LinearLayoutManager(this)
        friendRecyclerView.setHasFixedSize(true)

        friendArrayList = arrayListOf(
            Friend("1", "first", null),
            Friend("2", "second", null),
            Friend("3", "third", null),
            Friend("4", "aaaaaaaa", null),
            )

        friendRecyclerView.adapter = FriendAdapter(friendArrayList)
    }
}