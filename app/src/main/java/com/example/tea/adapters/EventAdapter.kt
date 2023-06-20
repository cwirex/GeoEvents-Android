package com.example.tea.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.R
import com.example.tea.adapters.viewHolders.EventViewHolder
import com.example.tea.menu.EventMenu
import com.example.tea.user.event.Event

class EventAdapter(private val eventList: List<Event>) : RecyclerView.Adapter<EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_event_list,
            parent,
            false
        )
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.eventTitle.text = eventList[position].title
    }

    override fun getItemCount(): Int = eventList.size

}