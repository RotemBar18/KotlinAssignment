package com.example.first_assignment_game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.first_assignment_game.interfaces.Callback_HighScoreClicked
import com.example.first_assignment_game.ui.LeaderBoardFragment
import com.example.first_assignment_game.ui.MapFragment
import com.example.first_assignment_game.utilities.BackgroundMusicPlayer
import com.example.sensorrclass.utilities.SingleSoundPlayer
import com.google.android.material.button.MaterialButton

class LeaderBoardActivity : AppCompatActivity() {

     private lateinit var  main_FRAME_list: FrameLayout
     private lateinit var  main_FRAME_map: FrameLayout

    private lateinit var mapFragment: MapFragment

    private lateinit var  highScoreFragment: LeaderBoardFragment

    private lateinit var  menu_BTN_back: MaterialButton

    private lateinit var ssp: SingleSoundPlayer




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leaderboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        BackgroundMusicPlayer.getInstance().playMusic()
        findViews()
        initViews()
    }

    override fun onPause() {
        super.onPause()
        BackgroundMusicPlayer.getInstance().pauseMusic()
    }

    override fun onResume() {
        super.onResume()
        BackgroundMusicPlayer.getInstance().playMusic()
    }

    private fun initViews() {
        ssp = SingleSoundPlayer(this)
        mapFragment = MapFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.main_FRAME_map, mapFragment)
            .commit()

        highScoreFragment = LeaderBoardFragment()
        highScoreFragment.highScoreItemClicked =
            object : Callback_HighScoreClicked {
            override fun HighScoreItemClicked(lat: Double, lon: Double) {
                mapFragment.zoom(lat, lon)
            }
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.main_FRAME_list, highScoreFragment)
            .commit()

        menu_BTN_back.setOnClickListener { view : View ->
            ssp.playSound(R.raw.button_sound)
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list)
        main_FRAME_map = findViewById(R.id.main_FRAME_map)
        menu_BTN_back = findViewById(R.id.menu_BTN_back)

    }
}