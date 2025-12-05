package com.example.first_assignment_game

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.first_assignment_game.utilities.Constants
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlin.jvm.java

class GameOverActivity : AppCompatActivity() {

    private lateinit var score_LBL_title: MaterialTextView

    private lateinit var score_BTN_newGame: AppCompatImageButton

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
        val bundle: Bundle? = intent.extras
        val message = bundle?.getString(Constants.BundleKeys.MESSAGE_KEY,"🤷‍♂️ Unknown")
        val score = bundle?.getInt(Constants.BundleKeys.SCORE_KEY,0)

        score_LBL_title.text = buildString {
            append(message)
            append("\n")
            append(score)
        }

        score_BTN_newGame.setOnClickListener { view : View ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun findViews() {
        score_BTN_newGame = findViewById(R.id.score_BTN_newGame)
        score_LBL_title = findViewById(R.id.score_LBL_title)
    }

}