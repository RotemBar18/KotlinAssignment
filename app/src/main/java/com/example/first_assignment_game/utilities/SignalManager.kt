package com.example.first_assignment_game.utilities

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import java.lang.ref.WeakReference
import kotlin.also
import kotlin.let

class SignalManager private constructor(cotext: Context){
    private val contextRef = WeakReference(cotext)
    enum class ToastLength(val length:Int){
        LONG(Toast.LENGTH_LONG),
        SHORT(Toast.LENGTH_SHORT)
    }

    companion object{
        @Volatile
        private var instance: SignalManager? = null

        fun init(context: Context): SignalManager{
            return SignalManager.Companion.instance ?: synchronized(this) {
                instance
                    ?: SignalManager(context).also { instance = it }
            }
        }

        fun getInstance(): SignalManager {
            return SignalManager.Companion.instance ?: throw kotlin.IllegalStateException(
                "SignalManager must be initialized by calling init(context) before use"
            )
        }
    }

    fun toast(text: String, duration: ToastLength){
        contextRef.get()?.let {
            context ->
            Toast
                .makeText(
                    context,
                    text,
                    duration.ordinal
                )
                .show()
        }
    }

    fun vibrate(){
        contextRef.get()?.let { context ->
        val vibrator: Vibrator =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            }else{
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                val SOSPattern = longArrayOf(
                    200,
                    100,
                    200
                )
                val waveformVibrationEffect = VibrationEffect
                    .createWaveform(
                        SOSPattern,
                        -1
                    )

                val oneShotVibrationEffect =
                    VibrationEffect
                        .createOneShot(
                            50,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )

                vibrator.vibrate(waveformVibrationEffect)
            }else{
                vibrator.vibrate(500)
            }
        }
    }
}

