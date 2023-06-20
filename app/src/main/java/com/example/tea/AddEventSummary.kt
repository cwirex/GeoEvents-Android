package com.example.tea

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.adapters.RowAdapter
import com.example.tea.adapters.RowData

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

class AddEventSummary : Fragment() {
    private var location: String? = null
    private var nFriends: String? = null
    private var tStart: String? = null
    private var title: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var rowAdapter: RowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            location = it.getString(ARG_PARAM1)
            nFriends = it.getString(ARG_PARAM2)
            tStart = it.getString(ARG_PARAM3)
            title = it.getString(ARG_PARAM4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_event_summary, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val rowDataList: MutableList<RowData> = setRows()

        rowAdapter = RowAdapter(rowDataList)
        recyclerView.adapter = rowAdapter

        return rootView
    }

    private fun setRows(): MutableList<RowData> {
        val list = mutableListOf<RowData>()
        if (title != null)
            list.add(RowData("Nazwa wydarzenia", title!!))
        if(nFriends != null)
            list.add(RowData("Liczba zaproszonych", nFriends!!))
        if(tStart != null)
            list.add(RowData("Początek imprezy", tStart!!))
        if(location != null)
            list.add(RowData("Miejsce", location!!))
        return list
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String, param4: String) =
            AddEventSummary().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                    putString(ARG_PARAM4, param4)
                }
            }
    }
}