package com.example.tea.adapters.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.R

class FriendViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val friendName: TextView = itemView.findViewById(R.id.friendItemName)
}