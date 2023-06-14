package com.example.tea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tea.databinding.ActivityAddEventBinding
import com.example.tea.map.IMap
import com.example.tea.map.MapFragment
import com.example.tea.map.Marker
import com.example.tea.user.event.Event
import com.example.tea.user.event.Location
import com.example.tea.user.event.TimeFrame

class AddEventActivity : AppCompatActivity(), IMap.OnLocationChangeListener {

    private lateinit var binding: ActivityAddEventBinding
    private lateinit var descriptionFragment: EventDescriptionFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var addFriendsToEventFragment: AddFriendsToEventFragment
    private lateinit var currentStep: AddEventStep
    private lateinit var nextButton: Button
    private var event: Event = Event()
    private var eventBufferData: EventBufferData = EventBufferData()
    private var currentMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        currentStep = AddEventStep.DESCRITPIONFILLING
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        descriptionFragment = EventDescriptionFragment()
        mapFragment = MapFragment()
        addFriendsToEventFragment = AddFriendsToEventFragment()
        nextButton = binding.submitEventButton

        setContentView(binding.root)
        replaceFragment(descriptionFragment)

        nextButton.setOnClickListener {
            when(currentStep) {

                AddEventStep.DESCRITPIONFILLING -> {
                    eventBufferData.title = descriptionFragment.getTitle()
                    eventBufferData.description = descriptionFragment.getDescription()
                    replaceFragment(mapFragment)
                    currentStep = AddEventStep.LOCATIONPICKING
                }

                AddEventStep.LOCATIONPICKING -> {
                    if (currentMarker != null)
                    {
                        eventBufferData.location = Location("przyklad", currentMarker!!)

                        replaceFragment(addFriendsToEventFragment)
                    }
                    else
                    {
                        Toast.makeText(this, "Pick location first !!!", Toast.LENGTH_SHORT).show()
                    }

                }

                else -> {

                }
            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.addEventFrameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onLocationChanged(latitude: Double, longitude: Double) {
        currentMarker = Marker(lat = latitude, lon = longitude)

        Toast.makeText(
            this@AddEventActivity,
            "Map marker on (${currentMarker!!.lat}, ${currentMarker!!.lon})",
            Toast.LENGTH_SHORT
        ).show()
    }

    private enum class AddEventStep { DESCRITPIONFILLING, LOCATIONPICKING, FRIENDPICKING }

    private class EventBufferData {
        lateinit var eid: String
        lateinit var ownerId: String
        lateinit var title: String
        lateinit var description: String
        lateinit var timeFrame: TimeFrame
        lateinit var location: Location
        lateinit var participants: MutableMap<String, Event.Participant>
    }
}