package com.example.tea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tea.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventAdapter = EventAdapter(mutableListOf())

        binding.rvEventItems.adapter = eventAdapter
        binding.rvEventItems.layoutManager = LinearLayoutManager(this)

        binding.btnAddEvent.setOnClickListener {
            val eventTitle = binding.etEventTitle.text.toString()
            if(eventTitle.isNotEmpty()) {
                val event = Event(eventTitle)
                eventAdapter.addEvent(event)
                binding.etEventTitle.text.clear()
            }
        }
        binding.btnDeleteDoneEvents.setOnClickListener {
            eventAdapter.deleteDoneEvents()
        }
    }

}