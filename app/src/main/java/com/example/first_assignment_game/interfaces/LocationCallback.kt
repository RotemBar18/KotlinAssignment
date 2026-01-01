package com.example.first_assignment_game.interfaces

interface LocationCallback {
    fun onLocation(lat: Double, lon: Double)
    fun onLocationError(message: String)
}