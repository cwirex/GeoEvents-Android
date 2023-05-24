package com.example.tea.adapters.viewHolders

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.R

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvEventTitle: TextView = itemView.findViewById(R.id.tvEventTitle)
    val cbDone: CheckBox = itemView.findViewById(R.id.cbDone)
}