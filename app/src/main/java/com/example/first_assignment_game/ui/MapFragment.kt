package com.example.first_assignment_game.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.first_assignment_game.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textview.MaterialTextView


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private val placeholder = LatLng(31.7768, 35.2055)



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(
            R.layout.fragment_map,
            container,
            false)
        findViews()
        return v
    }



    private fun findViews() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        map.clear()
        map.addMarker(MarkerOptions().position(placeholder).title("Placeholder Location"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(placeholder, 16f))
    }

    fun zoom(lat: Double, lon: Double) {
        val target = LatLng(lat, lon)
        googleMap?.clear()
        googleMap?.addMarker(MarkerOptions().position(target).title("High Score Location"))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(target, 15f))
    }





}