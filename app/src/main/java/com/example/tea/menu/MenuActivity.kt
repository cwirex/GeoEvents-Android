package com.example.tea.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tea.R
import com.example.tea.databinding.ActivityMenuBinding
import com.example.tea.map.IMap
import com.example.tea.map.MapFragment
import com.example.tea.map.Marker
import com.example.tea.user.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity(), IMap.OnLocationChangeListener {

    private lateinit var binding: ActivityMenuBinding
    var currentMarker: Marker? = null
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)

        if (Firebase.auth.uid != null)
            user = User(Firebase.auth.uid!!)

        setContentView(binding.root)
        replaceFragment(EventsMenuFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_events -> replaceFragment(EventsMenuFragment())
                R.id.menu_map -> {
                    val mapFragment = MapFragment()
                    replaceFragment(mapFragment)

                    // Show all events markers on map:
                    if (user != null) {
                        user!!.eventManager.getUserEvents { events ->
                            if (events != null)
                                mapFragment.makeMultipleEventMarkers(events.values)
                        }
                    }

                }

                R.id.menu_friends -> replaceFragment(FriendsMenuFragment())

                else -> {

                }

            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onLocationChanged(latitude: Double, longitude: Double) {
        currentMarker = Marker(lat = latitude, lon = longitude)

        Toast.makeText(
            this@MenuActivity,
            "(${String.format("%.2f", currentMarker!!.lat)}; ${
                String.format(
                    "%.2f",
                    currentMarker!!.lon
                )
            })",
            Toast.LENGTH_SHORT
        ).show()

    }
}