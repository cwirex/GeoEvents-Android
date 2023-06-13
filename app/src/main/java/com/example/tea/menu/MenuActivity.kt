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

class MenuActivity : AppCompatActivity(), IMap.OnLocationChangeListener {

    private lateinit var binding: ActivityMenuBinding
    var currentMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)

        setContentView(binding.root)
        replaceFragment(EventsMenuFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_events -> replaceFragment(EventsMenuFragment())
                R.id.menu_map -> replaceFragment(MapFragment())
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
            "Map marker on (${currentMarker!!.lat}, ${currentMarker!!.lon})",
            Toast.LENGTH_SHORT
        ).show()
    }
}