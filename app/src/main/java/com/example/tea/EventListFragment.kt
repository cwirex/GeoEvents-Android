package com.example.tea

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.adapters.EventAdapter
import com.example.tea.map.Marker
import com.example.tea.menu.EventMenu


class EventListFragment : Fragment() {
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event_list, container, false)
        eventRecyclerView = view.findViewById(R.id.rv_event_items)
        eventRecyclerView.layoutManager = LinearLayoutManager(activity)
        eventRecyclerView.setHasFixedSize(true)
        eventAdapter = EventAdapter(getEventList())
        eventRecyclerView.adapter = eventAdapter
        return view
    }

    private fun getEventList(): ArrayList<EventMenu> {
        return arrayListOf(
            EventMenu("jakis Event", Marker(0.0,0.0)),
            EventMenu("jakis Event", Marker(0.0,0.0))
        )
    }
}