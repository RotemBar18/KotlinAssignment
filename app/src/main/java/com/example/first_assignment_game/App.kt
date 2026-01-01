package com.example.first_assignment_game

import android.app.Application
import com.example.first_assignment_game.utilities.SharedPreferencesManager
import com.example.first_assignment_game.utilities.BackgroundMusicPlayer
import com.example.first_assignment_game.utilities.SignalManager
import com.example.sensorrclass.utilities.SingleSoundPlayer

class App: Application() {
    private lateinit var ssp: SingleSoundPlayer

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        BackgroundMusicPlayer.init(this)
        BackgroundMusicPlayer.getInstance().resourceId = R.raw.bg_music
        SignalManager.init(this)
        ssp = SingleSoundPlayer(this)
        ssp.playSound(R.raw.goku_hey)
    }

    override fun onTerminate() {
        super.onTerminate()
        BackgroundMusicPlayer.getInstance().stopMusic()
    }

}