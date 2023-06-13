package com.example.tea.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tea.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment() {

    lateinit var gmap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment

        supportMapFragment.getMapAsync { googleMap ->
            gmap = googleMap
            gmap.setOnMapClickListener { latLng ->
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title("${latLng.latitude} : ${latLng.longitude}")
                gmap.clear()
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                gmap.addMarker(markerOptions)
            }
        }

        return view
    }

}