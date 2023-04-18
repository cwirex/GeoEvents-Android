package com.example.tea

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private val events: MutableList<Event>
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEventTitle: TextView = itemView.findViewById(R.id.tvEventTitle)
        val cbDone: CheckBox = itemView.findViewById(R.id.cbDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_event,
            parent,
            false
        )
        return EventViewHolder(itemView)
    }

    fun addEvent(event: Event) {
        events.add(event)
        notifyItemInserted(events.size - 1)
    }

    fun deleteDoneEvents() {
        events.removeAll { event ->
            event.isChecked
        }
        notifyDataSetChanged()
    }

    private fun toggleStrikeThrough(tvEventTitle: TextView, isChecked: Boolean) {
        if (isChecked) {
            tvEventTitle.paintFlags = tvEventTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tvEventTitle.paintFlags = tvEventTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val curEvent = events[position]
        holder.tvEventTitle.text = curEvent.title
        holder.cbDone.isChecked = curEvent.isChecked
        toggleStrikeThrough(holder.tvEventTitle, curEvent.isChecked)
        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            toggleStrikeThrough(holder.tvEventTitle, isChecked)
            curEvent.isChecked = isChecked
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }
}
