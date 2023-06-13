package com.example.tea.map

interface IMap {
    interface OnLocationChangeListener {
        fun onLocationChanged(latitude: Double, longitude: Double)
    }

}