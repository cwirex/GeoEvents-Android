package com.example.tea.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tea.Event
import com.example.tea.R

import com.example.tea.adapters.viewHolders.EventViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException


class EventAdapter(
    options: FirestoreRecyclerOptions<Event>
) : FirestoreRecyclerAdapter<Event, EventViewHolder>(options) {
    private var isOnline = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_event,
            parent,
            false
        )
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int, model: Event) {
        holder.tvEventTitle.text = model.title
        holder.cbDone.isChecked = model.checked
        holder.cbDone.setOnClickListener {
            val isChecked = holder.cbDone.isChecked
            if (isOnline) {
                snapshots.getSnapshot(position).reference.update("checked", isChecked)
            } else {
                model.checked = isChecked
            }
        }
        if (model.checked) {
            holder.tvEventTitle.paintFlags = holder.tvEventTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.tvEventTitle.paintFlags = holder.tvEventTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun deleteCheckedItems(): Int {
        var itemsDeleted = 0
        for (i in 0 until itemCount) {
            val snapshot = snapshots.getSnapshot(i)
            val event = snapshot.toObject(Event::class.java)
            if (event?.checked == true) {
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
