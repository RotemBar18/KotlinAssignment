package com.example.first_assignment_game.utilities

import android.content.Context
import android.media.MediaPlayer
import java.lang.ref.WeakReference

class BackgroundMusicPlayer private constructor(context: Context){
    private val contentRef = WeakReference(context)
    private var mediaPlayer: MediaPlayer? = null

    var resourceId: Int = 0
        set(value) {
            field = value
            initMediaPlayer()
        }

    private fun initMediaPlayer() {
        if (mediaPlayer != null) {
            release()
        }
        mediaPlayer= MediaPlayer.create(
            contentRef.get(),
            resourceId
        )
        mediaPlayer?.isLooping = true

        mediaPlayer?.setVolume(0.35f,0.35f)
    }

    private fun release() {
        if (mediaPlayer == null) {
            return
        }
        try{
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (ex: IllegalStateException){
            ex.printStackTrace()
        }
    }


    companion object {
        @Volatile
        private var instance: BackgroundMusicPlayer? = null

        fun init(context: Context): BackgroundMusicPlayer {
            return instance ?: synchronized(this) {
                instance ?: BackgroundMusicPlayer(context).also {
                    instance = it
                }
            }
        }

        fun getInstance(): BackgroundMusicPlayer {
            return instance
                ?: throw IllegalStateException("BackgroundMusicPlayer must be initialized first by calling init(context)")

        }

    }
    fun playMusic() {
        if (mediaPlayer == null)
            return
        try {
            mediaPlayer?.start()
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }


    fun pauseMusic(){
        if (mediaPlayer == null)
            return
        try {
            mediaPlayer?.pause()
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }

    fun stopMusic(){
        if (mediaPlayer == null)
            return
        try {
            mediaPlayer?.stop()
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }

}