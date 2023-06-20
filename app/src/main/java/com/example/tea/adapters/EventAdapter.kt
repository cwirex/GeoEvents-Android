package com.example.tea.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tea.R
import com.example.tea.adapters.viewHolders.EventViewHolder
import com.example.tea.adapters.viewHolders.FriendViewHolder
import com.example.tea.menu.EventMenu
import com.example.tea.menu.FriendMenu
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class EventAdapter(options: FirestoreRecyclerOptions<EventMenu>)
: FirestoreRecyclerAdapter<EventMenu, EventViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_event_list,
            parent,
            false
        )
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int, model: EventMenu) {
        holder.eventTitle.text = model.title
    }
}