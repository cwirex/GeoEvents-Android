package com.example.tea.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.R
import com.example.tea.adapters.viewHolders.RowViewHolder

class RowAdapter(private val rowDataList: List<RowData>) : RecyclerView.Adapter<RowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return RowViewHolder(view)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val rowData = rowDataList[position]
        holder.headerTextView.text = rowData.key
        holder.valueTextView.text = rowData.value
    }

    override fun getItemCount(): Int {
        return rowDataList.size
    }
}

data class RowData(val key: String, val value: String)
