package com.example.tea.user.event

import android.util.Log
import com.example.tea.user.Database
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tea.user.User
import com.example.tea.user.invitation.Invitation
import com.example.tea.map.Marker
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

/** Holds user's events and manages them */
class EventManager(val user: User) : Database.Events {
    private val collectionRef = FirebaseFirestore.getInstance().collection("events")
    private val userRef = FirebaseFirestore.getInstance().collection("users").document(user.getId())
    private var events: MutableMap<String, Event>? = null
    private var eventsInfo: Map<String, Boolean>? = null

    companion object {
        private const val TAG = "EventManager"
    }

    /** Returns event, if it isn't in collection fetches from db */
    override fun getEvent(eid: String, callback: (event: Event?) -> Unit) {
        if (events?.containsKey(eid) == true) {
            callback(events!![eid])
        } else {
            collectionRef.document(eid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val eventData =
                            documentSnapshot.toObject(Database.Events.EventData::class.java)
                        if (eventData != null) {
                            val event = mapToEvent(eventData)
                            callback(event)
                        }
                    } else callback(null)
                }
                .addOnFailureListener {
                    callback(null)
                    // TODO Throw exception or pass?
                }
        }
    }

    /** Sends Friend to db and adds it to list.
     * If eid is empty, generates it in-place.
     * Returns event id */
    fun addEvent(event: Event): String {
        if (event.eid == "")
            event.eid =
                "${user.getId().drop(user.getId().length - 4)}${LocalDateTime.now().hashCode()}"
        addEvent(event.eid, mapToPojo(event))
        if (events != null) this.events!![event.eid] = event
        else events = mutableMapOf(event.eid to event)
        return event.eid
    }

    /** Transfers eventData to DB */
    override fun addEvent(eid: String, event: Database.Events.EventData) {
        collectionRef.document(eid)
            .set(event)
            .addOnFailureListener { e ->
                throw Exception("Exception on addEvent for $eid: $e")
            }
    }

    /** Updates event on DB (based on update map)*/
    override fun updateEvent(eid: String, updateMap: Map<String, Any>) {
        val isPojo = !updateMap.values.any { value ->
            value is Map<*, *> || value is List<*>
        }
        if (isPojo) {
            collectionRef.document(eid)
                .update(updateMap)
                .addOnFailureListener { e -> throw Exception("Failed to update document $eid: $e") }
        } else throw Exception("Cannot update with invalid pojo object")
    }

    /** Removes event from DB and local list */
    override fun deleteEvent(eid: String) {
        events?.remove(eid)  //safe
        collectionRef.document(eid)
            .delete()
            .addOnFailureListener { e -> throw Exception("Failed to delete document $eid: $e") }
    }

    /** Generates sample event*/
    fun getSampleEvent(): Event {
        return Event(
            eid = "",
            ownerId = user.getId(),
            title = "Friend Friend",
            description = "This is a sample event",
            timeFrame = TimeFrame(LocalDateTime.now(), LocalDateTime.now()),
            location = Location("Friend Location", Marker(0.0, 0.0)),
            participants = mutableMapOf(
                "user-id" to Event.Participant(
                    "user-id",
                    "user-nickname",
                    Invitation.Status.PENDING
                )
            )
        )
    }

    /** Maps Friend to EventData */
    private fun mapToPojo(event: Event): Database.Events.EventData {
        return Database.Events.EventData(
            eid = event.eid,
            owner = event.ownerId,
            title = event.title,
            desc = event.description,
            timeStart = event.timeFrame.start.toString(),
            timeEnd = event.timeFrame.end.toString(),
            locationName = event.location.name,
            long = event.location.position.lon,
            lat = event.location.position.lat,
            participants = event.participants.map { (k, v) ->
                k to v.status.ordinal
            }.toMap()
        )
    }

    /** Maps EventData to Friend */
    private fun mapToEvent(eventData: Database.Events.EventData): Event {
        val eid = eventData.eid
        val ownerId = eventData.owner
        val title = eventData.title
        val description = eventData.desc
        val start = LocalDateTime.parse(eventData.timeStart)
        val end = LocalDateTime.parse(eventData.timeEnd)
        val locationName = eventData.locationName
        val long = eventData.long
        val lat = eventData.lat
        val participants: MutableMap<String, Event.Participant> =
            eventData.participants.mapValues { (k, v) ->
                val status = Invitation.Status.values()[v]
                Event.Participant(
                    k,
                    "",
                    status
                ) //TODO Replace "" with the appropriate nickname value
            }.toMutableMap()

        val location = Location(locationName, Marker(lat, long))
        val timeFrame = TimeFrame(start, end)

        return Event(eid, ownerId, title, description, timeFrame, location, participants)
    }

    /** Returns user events in a callback */
    fun getUserEvents(callback: (Map<String, Event>?) -> Unit) {
        if (events != null) callback(events!!.toMap())    // if events are already fetched
        else fetchAndUpdateUserEvents(callback)
    }

    private fun fetchAndUpdateUserEvents(callback: (Map<String, Event>?) -> Unit) {
        if (eventsInfo == null) {
            getInfoAndUpdateUserEvents(callback)
        } else {
            val eventIds = eventsInfo!!.keys.toList()
            val events = mutableMapOf<String, Event>()

            val firestore = Firebase.firestore
            val fetchTasks = mutableListOf<Task<DocumentSnapshot>>()

            for (eventId in eventIds) {
                val fetchTask = firestore.collection("events")
                    .document(eventId)
                    .get()

                fetchTasks.add(fetchTask)
            }

            Tasks.whenAllComplete(fetchTasks)
                .addOnSuccessListener { taskList ->
                    for (task in taskList) {
                        if (task.isSuccessful) {
                            val document = task.result as DocumentSnapshot
                            val eventData = document.toObject(Database.Events.EventData::class.java)

                            eventData?.let { event ->
                                val eventObj = mapToEvent(event)
                                events[event.eid] = eventObj
                            }
                        }
                    }

                    callback(events)
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "fetchAndUpdateUserEvents: $exception")
                    callback(null)
                }
        }
    }

    private fun getInfoAndUpdateUserEvents(callback: (Map<String, Event>?) -> Unit) {
        user.userManager.getUserData { userData: Database.Users.UserData? ->
            if (userData == null) {
                callback(null)
            } else {
                this.eventsInfo = userData.events.toMap()
                fetchAndUpdateUserEvents(callback)
            }
        }
    }

    fun setEventsInfo(eventsInfo: Map<String, Boolean>) {
        this.eventsInfo = eventsInfo.toMap()
        fetchAndUpdateUserEvents { }
    }

    fun notifyEventStatusChanged(eid: String, status: Invitation.Status) {
        if (events?.containsKey(eid) == true) {
            events!![eid]?.participants?.get(user.getId())?.status = status
        } else {
            getInfoAndUpdateUserEvents {//suboptimal
                // TODO: onStatusChanged() -> update ui.events ?
            }
        }

        /* old
        else {
            when(status){
                Invitation.Status.REJECTED -> {
                    getInfoAndUpdateUserEvents {  }
                }
                Invitation.Status.ACCEPTED -> {
                    getInfoAndUpdateUserEvents {  }
                }
                Invitation.Status.EXPIRED -> {
                    getInfoAndUpdateUserEvents {  } //suboptimal...
                }
                else -> {throw Exception("Exception on EventManager.notifyEventStatusChanged($eid, ${status.name})")}
            }
        }
         */

    }


}
