package com.example.first_assignment_game

import android.content.Intent
import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.first_assignment_game.utilities.Constants
import com.example.first_assignment_game.utilities.BackgroundMusicPlayer
import com.example.sensorrclass.utilities.SingleSoundPlayer
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlin.jvm.java

class MenuActivity : AppCompatActivity() {

    private lateinit var menu_TGL_difficulty: SwitchMaterial

    private lateinit var menu_BTN_buttonMode: MaterialButton

    private lateinit var menu_BTN_tiltMode: MaterialButton

    private lateinit var menu_BTN_topTen: MaterialButton


    private var difficulty: String = "Slow"

    private lateinit var ssp: SingleSoundPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menu_LAYOUT_screen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()

    }
    override fun onResume() {
        super.onResume()
        BackgroundMusicPlayer.getInstance().playMusic()
    }


    override fun onPause() {
        super.onPause()
        BackgroundMusicPlayer.getInstance().pauseMusic()
    }
    private fun initViews() {
        ssp = SingleSoundPlayer(this)
        menu_TGL_difficulty.isChecked = false
        menu_TGL_difficulty.setOnCheckedChangeListener {btn, isChecked ->
            menu_TGL_difficulty.isChecked = isChecked
            ssp.playSound(R.raw.button_sound)
            if (isChecked)
                difficulty = "Fast"
            else
                difficulty = "Slow"
            menu_TGL_difficulty.text = difficulty
        }


        menu_BTN_tiltMode.setOnClickListener {
            ssp.playSound(R.raw.start_game)
            changeToGameActivity(difficulty,true)
        }
        menu_BTN_buttonMode.setOnClickListener{
            ssp.playSound(R.raw.start_game)
            changeToGameActivity(difficulty,false)
        }
        menu_BTN_topTen.setOnClickListener{
            ssp.playSound(R.raw.button_sound)
            changeToLeaderBoardActivity()
        }
    }




    private fun findViews() {
        menu_TGL_difficulty = findViewById(R.id.menu_TGL_difficulty)
        menu_BTN_buttonMode = findViewById(R.id.menu_BTN_buttonMode)
        menu_BTN_tiltMode = findViewById(R.id.menu_BTN_tiltMode)
        menu_BTN_topTen = findViewById(R.id.menu_BTN_topTen)
    }

     private fun changeToGameActivity(difficulty: String,  isTiltMode: Boolean) {
        val intent = Intent(this, GameActivity::class.java)
        var bundle = Bundle()
        bundle.putString(Constants.BundleKeys.DIFFICULTY_KEY, difficulty)
        bundle.putBoolean(Constants.BundleKeys.MODE_TILT_KEY, isTiltMode)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun changeToLeaderBoardActivity() {
        val intent = Intent(this, LeaderBoardActivity::class.java)
        startActivity(intent)
        finish()
    }
}


