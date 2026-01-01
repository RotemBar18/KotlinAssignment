package com.example.first_assignment_game.utilities

import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import com.example.first_assignment_game.interfaces.LocationCallback
import com.google.android.gms.location.LocationServices

class LocationDetector(
    private val activity: AppCompatActivity,
    private val locationCallback: LocationCallback

)    {

    private val fused = LocationServices.getFusedLocationProviderClient(activity)


    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun fetchLastLocation() {
        // permission must already be granted before calling this
        fused.lastLocation
            .addOnSuccessListener { loc ->
                if (loc != null) {
                    locationCallback.onLocation(loc.latitude, loc.longitude)
                }
                else locationCallback.onLocationError("Error fetching location")
            }
            .addOnFailureListener {
                locationCallback.onLocationError("Error fetching location")
            }
    }
    }
