package com.example.tea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tea.adapters.EventAdapter
import com.example.tea.adapters.FriendAdapter
import com.example.tea.databinding.ActivityEventListBinding
import com.example.tea.databinding.ActivityFriendsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EventListActivity : AppCompatActivity() {
    private lateinit var eventAdapter: EventAdapter
    private lateinit var binding: ActivityEventListBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)

        auth = Firebase.auth
        binding = ActivityEventListBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}