package com.example.first_assignment_game

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.first_assignment_game.model.HighScore
import com.example.first_assignment_game.model.LeaderBoard
import com.example.first_assignment_game.utilities.BackgroundMusicPlayer
import com.example.first_assignment_game.utilities.Constants
import com.example.first_assignment_game.utilities.SharedPreferencesManager
import com.example.sensorrclass.utilities.SingleSoundPlayer
import com.google.android.material.textview.MaterialTextView
import com.example.first_assignment_game.interfaces.LocationCallback
import com.example.first_assignment_game.utilities.LocationDetector
import com.google.gson.Gson
import kotlin.jvm.java

class GameOverActivity : AppCompatActivity() {

    private lateinit var score_LBL_title: MaterialTextView

    private lateinit var score_BTN_newGame: AppCompatImageButton

    private lateinit var highscore_BTN_send: AppCompatImageButton
    private lateinit var gameover_EDITTEXT_name: EditText
    private lateinit var gameover_LAYOUT_highscore: LinearLayout

    private var pendingName: String? = null
    private var pendingScore: Int? = null
    private var locationDetector: LocationDetector? = null

    val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                locationDetector?.fetchLastLocation()
            } else {
                // fallback: save without location
                saveHighScore(pendingName!!, pendingScore, 0.0, 0.0)
                gameover_LAYOUT_highscore.visibility = View.GONE
            }
        }

    private lateinit var ssp: SingleSoundPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gameover)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()
    }

    private fun initViews() {
        ssp = SingleSoundPlayer(this)
        val bundle: Bundle? = intent.extras
        val message = bundle?.getString(Constants.BundleKeys.MESSAGE_KEY,"🤷‍♂️ Unknown")
        val score = bundle?.getInt(Constants.BundleKeys.SCORE_KEY,0)

        pendingScore = score
        score_LBL_title.text = buildString {
            append(message)
            append("\n")
            append(score)
        }

        score_BTN_newGame.setOnClickListener { view : View ->
            ssp.playSound(R.raw.button_sound)
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
        gameover_LAYOUT_highscore.visibility = View.GONE
        highscore_BTN_send.setOnClickListener {
            ssp.playSound(R.raw.button_sound)

            val name = gameover_EDITTEXT_name.text?.toString()?.trim().orEmpty()
            if (name.isEmpty()) {
                gameover_EDITTEXT_name.error = "Please enter your name"
                return@setOnClickListener
            }

            pendingName = name
            pendingScore = score

            // Create detector
            locationDetector = LocationDetector(this, object : LocationCallback {
                override fun onLocation(lat: Double, lon: Double) {
                    saveHighScore(name, score, lat, lon)
                    Log.d("latlon", "$lat, $lon")
                    gameover_LAYOUT_highscore.visibility = View.GONE
                }

                override fun onLocationError(message: String) {
                    //pass
                }
            })

            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) {
                locationDetector?.fetchLastLocation()
            } else {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }


        BackgroundMusicPlayer.getInstance().pauseMusic()

        val isHighScore = checkIfHighScore(score)
        if (!isHighScore){
            ssp.playSound(R.raw.death)
        }else {
            if (score != null) {
                if (score > 9000){
                    ssp.playSound(R.raw.over9000)
                }else{
                    ssp.playSound(R.raw.highscore)
                }
            }
        }

    }

    private fun findViews() {
        score_BTN_newGame = findViewById(R.id.gameover_BTN_newGame)
        score_LBL_title = findViewById(R.id.gameover_LBL_title)
        highscore_BTN_send = findViewById(R.id.highscore_BTN_send)
        gameover_EDITTEXT_name = findViewById(R.id.gameover_EDITTEXT_name)
        gameover_LAYOUT_highscore = findViewById(R.id.gameover_LAYOUT_highscore)
    }

    private fun checkIfHighScore(score: Int?) : Boolean{
        val gson = Gson()

        val leaderBoardJson = SharedPreferencesManager
            .getInstance()
            .getString(Constants.SP_Keys.LEADERBOARD_KEY, "")

        val leaderBoard: LeaderBoard = if (leaderBoardJson.isNotEmpty()) {
            try {
                gson.fromJson(leaderBoardJson, LeaderBoard::class.java)
            } catch (e: Exception) {
                LeaderBoard(emptyList())
            }
        } else {
            LeaderBoard(emptyList())
        }
        val currentLeaderBoard = leaderBoard.leaderBoard.sortedByDescending { it.score }
        val lowestScoreInTop = currentLeaderBoard.getOrNull(
            Constants.LeaderBoardConstants.LEADERBOARD_ROWS - 1)?.score ?: Int.MIN_VALUE

        val isHighScore =
            currentLeaderBoard.size < Constants.LeaderBoardConstants.LEADERBOARD_ROWS ||
                    score!! > lowestScoreInTop
        if (isHighScore){
            showNameForm(score,currentLeaderBoard)
        }

        return isHighScore
    }

    private fun showNameForm(score: Int?, currentLeaderBoard: List<HighScore>) {
        gameover_LAYOUT_highscore.visibility = View.VISIBLE
    }




    private fun saveHighScore(name: String, score: Int? ,lat: Double, lon: Double) {

        val newHighScore = HighScore(name, score ?: 0, lat,lon)
        val gson = Gson()
        val key = Constants.SP_Keys.LEADERBOARD_KEY
        val existingJson = SharedPreferencesManager.getInstance().getString(key, "")
        val existingLeaderBoard = if (existingJson.isNotEmpty()) {
            try {
                gson.fromJson(existingJson, LeaderBoard::class.java)
            } catch (e: Exception) {
                LeaderBoard(emptyList())
            }
            } else {
                LeaderBoard(emptyList())
            }
        val updatedTop10 = (existingLeaderBoard.leaderBoard + newHighScore)
            .sortedByDescending { it.score }
            .take(10)

        SharedPreferencesManager.getInstance().putString(
            key,
            gson.toJson(LeaderBoard(updatedTop10))
        )
    }


}