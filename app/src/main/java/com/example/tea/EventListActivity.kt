package com.example.tea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.tea.adapters.EventAdapter
import com.example.tea.adapters.FriendAdapter
import com.example.tea.databinding.ActivityEventListBinding
import com.example.tea.databinding.ActivityFriendsBinding
import com.example.tea.map.Marker
import com.example.tea.menu.EventMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EventListActivity : AppCompatActivity() {
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        eventRecyclerView = findViewById(R.id.rv_event_items)
        eventRecyclerView.layoutManager = LinearLayoutManager(this)
        eventRecyclerView.setHasFixedSize(true)
        eventRecyclerView.adapter = EventAdapter(getEventList())
    }

    private fun getEventList(): ArrayList<EventMenu> {
        return arrayListOf(
            EventMenu("jakis Event", Marker(0.0,0.0)),
            EventMenu("jakis Event", Marker(0.0,0.0))
        )
    }
}