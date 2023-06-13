package com.example.tea.adapters.viewHolders

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.R

class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvFriendEmail: TextView = itemView.findViewById(R.id.tvFriendEmail)
    val cbFriendChecked: CheckBox = itemView.findViewById(R.id.cbFriendChecked)
}