package com.example.tea.adapters.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.R

class EventViewHolder(itemView: View, onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener {
            // this will be called only once.
            onItemClicked(bindingAdapterPosition)
        }
    }


    val eventTitle: TextView = itemView.findViewById(R.id.event_item_title)
}