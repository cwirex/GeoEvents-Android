package com.example.tea

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tea.databinding.ActivityAddEventBinding
import com.example.tea.map.IMap
import com.example.tea.map.MapFragment
import com.example.tea.map.Marker
import com.example.tea.user.User
import com.example.tea.user.event.Event
import com.example.tea.user.event.Location
import com.example.tea.user.event.TimeFrame
import com.example.tea.user.friend.Friend
import com.example.tea.user.invitation.Invitation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AddEventActivity : AppCompatActivity(), IMap.OnLocationChangeListener {

    private lateinit var binding: ActivityAddEventBinding
    private lateinit var descriptionFragment: EventDescriptionFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var addFriendsToEventFragment: AddFriendsToEventFragment
    private lateinit var currentStep: AddEventStep
    private lateinit var nextButton: Button
    private var eventBufferData: EventBufferData = EventBufferData()
    private var currentMarker: Marker? = null
    private lateinit var user: User
    private lateinit var locationName: String
    private lateinit var eventStartDate: LocalDate

    private enum class AddEventStep { DESCRITPIONFILLING, LOCATIONPICKING, FRIENDPICKING, TIMEPICKING, SUMMARY, SEND }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = Firebase.auth.uid?.let { User(it) }!!

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
            when (currentStep) {
                AddEventStep.DESCRITPIONFILLING -> {
                    val title = descriptionFragment.getTitle()
                    val desc = descriptionFragment.getDescription()
                    val name = descriptionFragment.getName()
                    if (title.isNotEmpty() and desc.isNotEmpty() and name.isNotEmpty()) {
                        eventBufferData.title = title
                        eventBufferData.description = desc
                        locationName = name
                        replaceFragment(mapFragment)
                        currentStep = AddEventStep.LOCATIONPICKING
                    } else {
                        Toast.makeText(
                            this@AddEventActivity,
                            "Fields cannot be empty!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                AddEventStep.LOCATIONPICKING -> {
                    if (currentMarker != null) {
                        eventBufferData.location = Location(locationName, currentMarker!!)
                        currentStep = AddEventStep.FRIENDPICKING
                        nextButton.callOnClick()
                    } else {
                        Toast.makeText(this, "Pick location first !!!", Toast.LENGTH_SHORT).show()
                    }
                }

                AddEventStep.FRIENDPICKING -> {
                    Toast.makeText(this@AddEventActivity, "Fetching friends...", Toast.LENGTH_SHORT)
                        .show()
                    user.friendManager.getUserFriends { friends ->
                        eventBufferData.participants = mutableMapOf()
                        friends?.values?.forEach { friend: Friend ->
                            eventBufferData.participants[friend.id] =
                                Event.Participant(
                                    friend.id,
                                    friend.nickname,
                                    Invitation.Status.PENDING
                                )
                        }
                        val nFriends: Int = if (friends?.size != null) friends.size else 0
                        Toast.makeText(
                            this,
                            "Added $nFriends friends to this event",
                            Toast.LENGTH_SHORT
                        ).show()

                        currentStep = AddEventStep.TIMEPICKING
                        nextButton.callOnClick()
                    }
                }

                AddEventStep.TIMEPICKING -> {
                    val datePicker = DatePickerDialog(
                        this@AddEventActivity, 0,
                        { datePicker, y, m, d ->
                            eventStartDate = LocalDate.of(y, m, d)
                            val timePicker = TimePickerDialog(
                                this@AddEventActivity, 0,
                                { timePicker, hh, mm ->
                                    eventBufferData.timeFrame = TimeFrame(
                                        start = LocalDateTime.of(
                                            eventStartDate,
                                            LocalTime.of(hh, mm)
                                        ),
                                        end = LocalDateTime.now()   //todo: add step for end date
                                    )
                                    currentStep = AddEventStep.SUMMARY
                                    nextButton.callOnClick()
                                }, 12, 0, true
                            )
                            timePicker.show()
                        }, 2023, 6, 21
                    )
                    datePicker.show()
                }

                AddEventStep.SUMMARY -> {
                    eventBufferData.eid = ""    // OK, will be set up by EventManager
                    eventBufferData.ownerId = user.getId()
                    val locationString = eventBufferData.location.name
                    val nFriends = eventBufferData.participants.size.toString()
                    val startString = eventBufferData.timeFrame.start.toString()

                    val summaryFragment =
                        AddEventSummary.newInstance(
                            locationString,
                            nFriends,
                            startString,
                            eventBufferData.title
                        )
                    replaceFragment(summaryFragment)


                    currentStep = AddEventStep.SEND
                }

                AddEventStep.SEND -> {
                    val event = Event(
                        eventBufferData.eid,
                        eventBufferData.ownerId,
                        eventBufferData.title,
                        eventBufferData.description,
                        eventBufferData.timeFrame,
                        eventBufferData.location,
                        eventBufferData.participants
                    )
                    val eid = user.eventManager.addEvent(event)
                    Toast.makeText(
                        this@AddEventActivity,
                        "Created event #${eid}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    currentStep = AddEventStep.DESCRITPIONFILLING
                }
            }
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