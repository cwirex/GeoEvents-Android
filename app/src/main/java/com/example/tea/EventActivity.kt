package com.example.tea

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tea.adapters.EventAdapter
import com.example.tea.databinding.ActivityEventBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventActivity : AppCompatActivity() {
    private lateinit var eventAdapter: EventAdapter
    private lateinit var binding: ActivityEventBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = db.collection(REF).orderBy("timestamp")
        val options = FirestoreRecyclerOptions.Builder<Event>()
            .setQuery(query, Event::class.java)
            .setLifecycleOwner(this@EventActivity).build()
        eventAdapter = EventAdapter(options)
        binding.rvEventItems.adapter = eventAdapter
        binding.rvEventItems.layoutManager = LinearLayoutManager(this)

        binding.btnAddEvent.setOnClickListener(addEventListener)
        binding.btnDeleteDoneEvents.setOnClickListener(deleteEventListener)
    }

    private val addEventListener = View.OnClickListener {
        val eventTitle = binding.etEventTitle.text.toString()
        if(eventTitle.isNotEmpty()) {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmssSSS"))
            val event = Event(title=eventTitle, timestamp=timestamp)
            db.collection(REF)
                .document(timestamp)
                .set(event)
                .addOnSuccessListener { _ ->
                    Log.d(TAG, "DocumentSnapshot added with ID: $timestamp")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            binding.etEventTitle.text.clear()
        }
    }

    private val deleteEventListener = View.OnClickListener {
        val nDeleted = eventAdapter.deleteCheckedItems()
        if (nDeleted > 0){
            Toast.makeText(this@EventActivity,
                "Deleted $nDeleted item${if (nDeleted > 1) "s" else ""}",
                Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val TAG = "EventActivity"
        const val REF = "events"
    }
}