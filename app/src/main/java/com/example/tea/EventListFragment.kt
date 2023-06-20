package com.example.tea

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.adapters.EventAdapter
import com.example.tea.map.MapFragment
import com.example.tea.user.User
import com.example.tea.user.event.Event
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class EventListFragment : Fragment() {
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventRecyclerView: RecyclerView
    private val eventsList: MutableList<Event> = mutableListOf()
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uid = Firebase.auth.uid
        if (uid != null)
            user = User(uid)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_list, container, false)
        eventRecyclerView = view.findViewById(R.id.rv_event_items)
        eventRecyclerView.layoutManager = LinearLayoutManager(activity)
        eventAdapter = EventAdapter(eventsList) { event ->
            var mapFragment = MapFragment(event.location.position)
            parentFragmentManager.beginTransaction()
                .add(R.id.event_list_frame_layout, mapFragment)
                .commit()

        }
        eventRecyclerView.adapter = eventAdapter
        updateEvenList()

        return view
    }

    private fun updateEvenList() {
        user.eventManager.getUserEvents { userEvents ->
            if (userEvents != null) {
                eventsList.clear()
                eventsList.addAll(userEvents.values)
                eventAdapter.notifyDataSetChanged()
            }
        }
    }

}