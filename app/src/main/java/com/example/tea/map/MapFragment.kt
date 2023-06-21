package com.example.tea.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tea.R
import com.example.tea.user.event.Event
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment(private val pickedEventMarker: Marker? = null) : Fragment() {
    private lateinit var locationClient: FusedLocationProviderClient
    lateinit var gmap: GoogleMap
    private var locationChangeListener: IMap.OnLocationChangeListener? = null


    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        locationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment

        supportMapFragment.getMapAsync { googleMap ->
            gmap = googleMap
            checkPermissions()
            if (pickedEventMarker != null) {
                makeMarker(
                    LatLng(pickedEventMarker.lat, pickedEventMarker.lon),
                    "Event location",
                    clear = false
                )
            }
            gmap.setOnMapClickListener { latLng ->
                makeMarker(latLng)
                notifyLocationChanged(latLng)
            }

            // todo: notify map ready?
        }



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permissions are not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        updateUserLocation(location)
                    }
                }
        } else {
            Toast.makeText(
                requireContext(),
                "Map won't work without gps permission!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUserLocation(location: Location) {
        val pos = LatLng(location.latitude, location.longitude)

        notifyLocationChanged(pos)
        makeMarker(latLng = pos, "Last known location")

        //todo: Update user's location
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Map won't work without gps permission!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun makeMultipleEventMarkers(events: Collection<Event>) {
        events.forEach { event: Event ->
            val latLng = LatLng(
                event.location.position.lat,
                event.location.position.lon
            )
            makeMarker(
                latLng,
                "${event.title} (${event.location.name})",
                clear = false
            )
        }
    }

    fun makeMarker(latLng: LatLng, title: String? = null, clear: Boolean = true) {
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        if (title != null) {
            markerOptions.title(title)
        } else {
            markerOptions.title("${latLng.latitude} : ${latLng.longitude}")
        }
        if (clear) gmap.clear()
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