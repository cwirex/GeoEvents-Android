package com.example.tea.adapters.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.R

class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val headerTextView: TextView = itemView.findViewById(R.id.row_header)
    val valueTextView: TextView = itemView.findViewById(R.id.row_value)
}
