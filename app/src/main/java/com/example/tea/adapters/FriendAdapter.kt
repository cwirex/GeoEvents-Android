package com.example.tea.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.R
import com.example.tea.adapters.viewHolders.FriendViewHolder
import com.example.tea.user.friend.Friend

class FriendAdapter(private val friendList: ArrayList<Friend>) : RecyclerView.Adapter<FriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)

        return FriendViewHolder(itemView)
    }

    override fun getItemCount(): Int = friendList.size

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friendList[position]
        holder.friendName.text = friend.nickname
    }
}