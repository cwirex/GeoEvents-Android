package com.example.tea.menu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.tea.R

class FriendsMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friends_menu, container, false)
        val friendListButton = view.findViewById<Button>(R.id.friendsListButton)

        friendListButton.setOnClickListener {
            val intent = Intent(activity, FriendsMenuActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
