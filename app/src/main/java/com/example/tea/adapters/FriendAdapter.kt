package com.example.tea.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tea.sample.Friend
import com.example.tea.R

import com.example.tea.adapters.viewHolders.FriendViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException


class FriendAdapter(
    options: FirestoreRecyclerOptions<Friend>
) : FirestoreRecyclerAdapter<Friend, FriendViewHolder>(options) {
    private var isOnline = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_event,
            parent,
            false
        )
        return FriendViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int, model: Friend) {
        holder.tvFriendEmail.text = model.email
        holder.cbFriendChecked.isChecked = model.checked
        holder.cbFriendChecked.setOnClickListener {
            val isChecked = holder.cbFriendChecked.isChecked
            if (isOnline) {
                snapshots.getSnapshot(position).reference.update("checked", isChecked)
            } else {
                model.checked = isChecked
            }
        }
        if (model.checked) {
            holder.tvFriendEmail.paintFlags = holder.tvFriendEmail.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.tvFriendEmail.paintFlags = holder.tvFriendEmail.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun deleteCheckedItems(): Int {
        var itemsDeleted = 0
        for (i in 0 until itemCount) {
            val snapshot = snapshots.getSnapshot(i)
            val friend = snapshot.toObject(Friend::class.java)
            if (friend?.checked == true) {
                snapshot.reference.delete()
                itemsDeleted += 1
            }
        }
        notifyDataSetChanged()
        return itemsDeleted
    }

    override fun onDataChanged() {
        super.onDataChanged()
        if (itemCount == 0) {
            // Handle empty state
        } else {
            // Handle non-empty state
        }
    }

    fun setIsOnline(isOnline: Boolean) {
        this.isOnline = isOnline
    }

    override fun onError(e: FirebaseFirestoreException) {
        super.onError(e)
        // Handle error
    }
}
