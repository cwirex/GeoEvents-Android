package com.example.tea.menu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.tea.MainActivity
import com.example.tea.R
import com.example.tea.auth.login.ui.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FriendsMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friends_menu, container, false)
        val friendListButton: Button = view.findViewById(R.id.friendsListButton)
        val logoutButton: Button = view.findViewById(R.id.btn_logout)

        friendListButton.setOnClickListener {
            val intent = Intent(activity, FriendsMenuActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            Firebase.auth.signOut()
            startActivity(intent)
        }

        return view
    }
}
