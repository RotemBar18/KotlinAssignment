package com.example.first_assignment_game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.first_assignment_game.logic.GameManager
import com.example.first_assignment_game.utilities.Constants
import com.example.first_assignment_game.utilities.SignalManager
import com.example.first_assignment_game.model.DataManager
import java.util.Timer
import java.util.TimerTask
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    private lateinit var gameManager: GameManager
    private lateinit var main_IMG_hearts : Array<AppCompatImageView>
    private lateinit var main_GRID_Board: Array<FrameLayout>
    private lateinit var main_BTN_Left: AppCompatImageButton
    private lateinit var main_BTN_Right: AppCompatImageButton
    private lateinit var playerView: AppCompatImageView

    private lateinit var main_LAYOUT_screen: RelativeLayout


    private lateinit var energyBlastViews: Array<AppCompatImageView>

    private lateinit var timer: Timer
    private var startTime: Long = 0
    private var timerOn: Boolean = false






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_LAYOUT_screen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        initViews()
        SignalManager.init(this)
        gameManager.startGame()
        startTimer()
    }

    private fun initViews() {
        initMainScreen()
        main_BTN_Left.setOnClickListener {
            view: View -> if(gameManager.movePlayer(-1))
            updatePlayerPositionOnBoard()
        }
        main_BTN_Right.setOnClickListener{
            view: View ->  if(gameManager.movePlayer(1))
            updatePlayerPositionOnBoard()

        }
        initPlayerView()
        initEnergyBlastViews()

    }

    private fun initMainScreen() {
        main_LAYOUT_screen.setBackgroundResource(DataManager.getRandomBackground())
    }


    private fun initPlayerView() {
        playerView = AppCompatImageView(this).apply {
            setImageResource(R.drawable.player)
            layoutParams = GridLayout.LayoutParams(
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
        }
        val row = Constants.PlayerConstants.PLAYER_START_ROW
        val col = Constants.PlayerConstants.PLAYER_START_COL

        val index = row * Constants.GameConstants.COLS + col
        main_GRID_Board[index].addView(playerView)

    }

    private fun initEnergyBlastViews() {
        energyBlastViews = Array(main_GRID_Board.size) { index ->
            AppCompatImageView(this).apply {
                setImageResource(R.drawable.energy_blast)
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                visibility = View.GONE
            }.also { img ->
                main_GRID_Board[index].addView(img)
            }
        }
    }

    private fun findViews() {
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
        main_GRID_Board = arrayOf(
            findViewById(R.id.cell_0_0),
            findViewById(R.id.cell_0_1),
            findViewById(R.id.cell_0_2),
            findViewById(R.id.cell_1_0),
            findViewById(R.id.cell_1_1),
            findViewById(R.id.cell_1_2),
            findViewById(R.id.cell_2_0),
            findViewById(R.id.cell_2_1),
            findViewById(R.id.cell_2_2),
            findViewById(R.id.cell_3_0),
            findViewById(R.id.cell_3_1),
            findViewById(R.id.cell_3_2),
            findViewById(R.id.cell_4_0),
            findViewById(R.id.cell_4_1),
            findViewById(R.id.cell_4_2)
        )
        main_BTN_Left = findViewById(R.id.main_BTN_left)
        main_BTN_Right = findViewById(R.id.main_BTN_right)
        main_LAYOUT_screen = findViewById(R.id.main_LAYOUT_screen)
    }

    private fun stopTimer() {
        timerOn = false
        timer.cancel()
    }

    private fun startTimer() {
        if(!timerOn){
            timerOn = true
            startTime = System.currentTimeMillis()
            timer =Timer()
            timer.schedule(object : TimerTask(){
                override fun run() {
                    val currentTime = System.currentTimeMillis()
                    if(!gameManager.updateGame()){
                        stopTimer()
                        changeActivity("Game Over!\nEnergy Level:",gameManager.score)
                    }

                    runOnUiThread {
                        if (gameManager.prevLives > gameManager.lives){
                            gameManager.prevLives = gameManager.lives

                            SignalManager
                                .getInstance()
                                .toast(
                                    " Lives Left: "+gameManager.lives.toString(),
                                    SignalManager.ToastLength.SHORT
                                )
                            SignalManager.getInstance().vibrate()
                        }
                        if (gameManager.lives == 0) {
                            playerView.setImageResource(R.drawable.player_dead)
                        }
                        updateEnergyBlastsPositionOnBoard()
                        updateHearts()
                    }

                }
            },0, Constants.TimerConstants.DELAY)
        }
    }

    private fun updatePlayerPositionOnBoard() {
        val row = gameManager.playerRow
        val col = gameManager.playerIndex
        val prevCol= gameManager.playerPrevIndex
        val prevIndex = row * Constants.GameConstants.COLS + prevCol
        val index = row * Constants.GameConstants.COLS + col
        if (prevIndex != index){
            main_GRID_Board[prevIndex].removeView(playerView)
            main_GRID_Board[index].addView(playerView)
        }

    }

    private fun updateEnergyBlastsPositionOnBoard() {
        val grid = gameManager.grid
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                val index = i * Constants.GameConstants.COLS + j
                if (grid[i][j] == 1) {
                    energyBlastViews[index].visibility = View.VISIBLE
                }else{
                    energyBlastViews[index].visibility = View.GONE
                }
            }
        }
    }
    private fun updateHearts(){
       for(i in main_IMG_hearts.indices){
           if(i < gameManager.lives){
               main_IMG_hearts[i].visibility = View.VISIBLE
           }else{
               main_IMG_hearts[i].visibility = View.GONE
           }
       }
    }

     private fun changeActivity(message: String, score: Int) {
        val intent = Intent(this, GameOverActivity::class.java)
        var bundle = Bundle()
        bundle.putString(Constants.BundleKeys.MESSAGE_KEY, message)
        bundle.putInt(Constants.BundleKeys.SCORE_KEY, score)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


}