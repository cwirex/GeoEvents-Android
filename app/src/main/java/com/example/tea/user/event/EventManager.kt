package com.example.tea.user.event
import android.nfc.FormatException
import com.example.tea.user.Database
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tea.user.User
import com.example.tea.user.model.Marker
import com.google.android.gms.tasks.OnFailureListener
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

/** Holds user's events and manages them */
class EventManager(val user: User) : Database.Events {
    private val collectionRef = FirebaseFirestore.getInstance().collection("events")
    private val userRef = FirebaseFirestore.getInstance().collection("users").document(user.getId())
    private val events: MutableMap<String, Event> = mutableMapOf()
    override fun getEvent(eid: String, callback: (event: Event?) -> Unit) {
        collectionRef.document(eid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val eventData = documentSnapshot.toObject(Database.Events.EventData::class.java)
                    if (eventData != null) {
                        val event = mapToEvent(eventData)
                        callback(event)
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Error fetching document
                // Handle the exception
                callback(null)
            }



    }

/** Sends Event to db. If eid is empty, generates it in-place.
 * Returns event id */
    fun addEvent(event: Event): String{
        if(event.eid == "")
            event.eid = "${user.getId().drop(user.getId().length-4)}${LocalDateTime.now().hashCode()}"
        addEvent(event.eid, mapToPojo(event))
        return event.eid
    }

    override fun addEvent(eid: String, event: Database.Events.EventData) {
        collectionRef.document(eid)
            .set(event)
            .addOnFailureListener { e ->
               throw Exception("Exception on addEvent for $eid: $e")
            }
    }

    override fun updateEvent(eid: String, updateMap: Map<String, Any>) {
        val isPojo = !updateMap.values.any { value ->
            value is Map<*, *> || value is List<*>
        }
        if(isPojo) {
            collectionRef.document(eid)
                .update(updateMap)
                .addOnFailureListener { e -> throw Exception("Failed to update document $eid: $e") }
        }
        else throw Exception("Cannot update with invalid pojo object")
    }

    override fun deleteEvent(eid: String) {
        collectionRef.document(eid)
            .delete()
            .addOnFailureListener { e -> throw Exception("Failed to delete document $eid: $e") }
    }

    fun getSampleEvent(): Event{
        return Event(
            eid = "",
            ownerId = "owner-id",
            title = "Sample Event",
            description = "This is a sample event",
            timeFrame = TimeFrame(LocalDateTime.now(), LocalDateTime.now()),
            location = Location("Sample Location", Marker(0.0, 0.0)),
            participants = mutableMapOf("user-id" to Event.Participant("user-id", "user-nickname", Event.ParticipantStatus.PENDING))
        )
    }

    private fun mapToPojo(event: Event): Database.Events.EventData {
        return Database.Events.EventData(
            eid = event.eid,
            owner = event.ownerId,
            title = event.title,
            desc = event.description,
            timeStart = event.timeFrame.start.toString(),
            timeEnd = event.timeFrame.end.toString(),
            locationName = event.location.name,
            long = event.location.position.long,
            lat = event.location.position.lat,
            participants = event.participants.map{(k, v) ->
                k to v.status.ordinal
            }.toMap()
        )
    }

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
        val participants: MutableMap<String, Event.Participant> = eventData.participants.mapValues { (k, v) ->
            val status = Event.ParticipantStatus.values()[v]
            Event.Participant(k, "", status) //TODO Replace "" with the appropriate nickname value
        }.toMutableMap()

        val location = Location(locationName, Marker(lat, long))
        val timeFrame = TimeFrame(start, end)

        return Event(eid, ownerId, title, description, timeFrame, location, participants)
    }

    fun addSampleEvent(){
        val event = getSampleEvent()
        addEvent(event)
    }

    fun getUserEvents(): HashMap<String, Event> {
        return HashMap(events)
    }

}
