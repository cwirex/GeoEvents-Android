package com.example.tea.adapters.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.R

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val eventTitle: TextView = itemView.findViewById(R.id.event_item_title)
}