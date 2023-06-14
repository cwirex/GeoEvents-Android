package com.example.tea.menu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.tea.R

class EventsMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_events_menu, container, false)
        val addEventButton = view.findViewById<Button>(R.id.buttonAddEvent)

        addEventButton.setOnClickListener {
//            val intent = Intent(activity, FriendsMenuActivity::class.java)
//            startActivity(intent)
        }

        return view
    }

}