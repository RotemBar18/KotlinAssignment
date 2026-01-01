package com.example.first_assignment_game.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.first_assignment_game.interfaces.TiltCallback
import kotlin.math.abs

class TiltDetector(context: Context,private val tiltCallback: TiltCallback) {
    private val sensorManager = context.getSystemService(
        Context.SENSOR_SERVICE) as SensorManager

    private val sensor = sensorManager.getDefaultSensor(
        Sensor.TYPE_ACCELEROMETER)


    private var timeStamp : Long = 0L

    private lateinit var sensorEventListener: SensorEventListener

    init {
            initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //Pass
            }

            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val z = event.values[2]
                calculateTilt(x, z)
            }

        }

    }


    private fun calculateTilt(x: Float, z: Float) {
        if (System.currentTimeMillis() - timeStamp >= Constants.TiltConstants.TILT_DELAY){
            timeStamp = System.currentTimeMillis()
            if (x >= Constants.TiltConstants.TILT_THRESH_X){
                tiltCallback?.tiltLeft()
            }else if (x <= -Constants.TiltConstants.TILT_THRESH_X){
                tiltCallback?.tiltRight()
            }
            if (z >= Constants.TiltConstants.TILT_THRESH_Y){
                tiltCallback?.tiltFront()
            }
            if (z <= Constants.TiltConstants.TILT_THRESH_Y){
                tiltCallback?.tiltBack()
            }
        }
    }

    fun start(){
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun stop(){
        sensorManager.unregisterListener(
            sensorEventListener,
            sensor
        )
    }
}