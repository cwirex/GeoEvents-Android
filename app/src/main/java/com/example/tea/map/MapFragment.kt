package com.example.tea.map

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tea.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment() {
    lateinit var gmap: GoogleMap
    private var locationChangeListener: IMap.OnLocationChangeListener? = null

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
                makeMarker(latLng)
                notifyLocationChanged(latLng)
            }
        }

        return view
    }

    private fun makeMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("${latLng.latitude} : ${latLng.longitude}")
        gmap.clear()
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
        gmap.addMarker(markerOptions)
    }

    private fun notifyLocationChanged(latLng: LatLng) {
        locationChangeListener?.onLocationChanged(
            latitude = latLng.latitude,
            longitude = latLng.longitude
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            locationChangeListener = context as IMap.OnLocationChangeListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnLocationChangeListener")
        }
    }
}