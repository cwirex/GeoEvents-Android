package com.example.tea

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class EventDescriptionFragment : Fragment() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var nameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_event_description, container, false)
        titleEditText = view.findViewById(R.id.event_title_edit_text)
        descriptionEditText = view.findViewById(R.id.event_description_edit_text)
        nameEditText = view.findViewById(R.id.event_name_edit_text)

        return view
    }

    public fun getTitle(): String = titleEditText.text.toString()

    public fun getDescription(): String = descriptionEditText.text.toString()

    fun getName(): String = nameEditText.text.toString()
}