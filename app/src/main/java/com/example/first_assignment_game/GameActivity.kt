package com.example.first_assignment_game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.first_assignment_game.interfaces.TiltCallback
import com.example.first_assignment_game.logic.GameManager
import com.example.first_assignment_game.utilities.Constants
import com.example.first_assignment_game.utilities.SignalManager
import com.example.first_assignment_game.model.DataManager
import com.example.first_assignment_game.utilities.BackgroundMusicPlayer
import com.example.first_assignment_game.utilities.TiltDetector
import com.example.sensorrclass.utilities.SingleSoundPlayer
import java.util.Timer
import java.util.TimerTask
import kotlin.jvm.java

class GameActivity : AppCompatActivity() {

    private lateinit var gameManager: GameManager
    private lateinit var game_IMG_hearts : Array<AppCompatImageView>
    private lateinit var game_GRID_Board: Array<FrameLayout>
    private lateinit var game_BTN_Left: AppCompatImageButton
    private lateinit var game_BTN_Right: AppCompatImageButton
    private lateinit var playerView: AppCompatImageView

    private lateinit var game_LAYOUT_screen: RelativeLayout

    private lateinit var game_LBL_odometer: AppCompatTextView



    private lateinit var gameDifficulty: String




    private lateinit var energyBlastViews: Array<AppCompatImageView>
    private lateinit var senzuBeansViews: Array<AppCompatImageView>
    private lateinit var coinsViews: Array<AppCompatImageView>

    private lateinit var timer: Timer
    private var startTime: Long = 0
    private var timerOn: Boolean = false

    private var tiltMode = false
    private lateinit var tiltDetector: TiltDetector

    private lateinit var ssp: SingleSoundPlayer

    private  var timerDelay : Long = Constants.TimerConstants.EASY_DELAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game_LAYOUT_screen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        gameManager = GameManager(game_IMG_hearts.size)
        initViews()
        gameManager.startGame()
        startTimer()
        ssp = SingleSoundPlayer(this)
    }

    override fun onResume() {
        super.onResume()
        startTimer()
        if (tiltMode) tiltDetector.start()
        timerOn = true
        BackgroundMusicPlayer.getInstance().playMusic()
    }

    override fun onPause() {
        super.onPause()
        stopTimer()
        if (tiltMode) tiltDetector.stop()
        timerOn = false
        BackgroundMusicPlayer.getInstance().pauseMusic()

    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras
        gameDifficulty = bundle?.getString(Constants.BundleKeys.DIFFICULTY_KEY,"Slow").toString()
        tiltMode = bundle?.getBoolean(Constants.BundleKeys.MODE_TILT_KEY, false) ?: false
        BackgroundMusicPlayer.getInstance().playMusic()

        initMainScreen()
        initButtons()
        initPlayerView()
        initEnergyBlastViews()
        initSenzuBeansViews()
        initcoinsViews()

    }



    private fun initMainScreen() {
        game_LAYOUT_screen.setBackgroundResource(DataManager.getRandomBackground())
    }


    private fun initButtons() {
        if (tiltMode){
            game_BTN_Right.visibility = View.GONE
            game_BTN_Left.visibility = View.GONE
            game_BTN_Right.isClickable = false
            game_BTN_Left.isClickable = false
            tiltDetector = TiltDetector(this,object : TiltCallback{
                override fun tiltLeft() {
                    runOnUiThread {
                        if (gameManager.movePlayer(-1)) updatePlayerPositionOnBoard()
                    }
                }
                override fun tiltRight() {
                    runOnUiThread {
                        if (gameManager.movePlayer(1)) updatePlayerPositionOnBoard()
                    }
                }

                override fun tiltFront() {
                    setGameSpeed(Constants.TimerConstants.HARD_DELAY)
                }

                override fun tiltBack() {
                    setGameSpeed(Constants.TimerConstants.EASY_DELAY)
                }

            })
        }else {
            game_BTN_Left.setOnClickListener {
                    view: View -> if(gameManager.movePlayer(-1))
                updatePlayerPositionOnBoard()
            }
            game_BTN_Right.setOnClickListener{
                    view: View ->  if(gameManager.movePlayer(1))
                updatePlayerPositionOnBoard()

            }
            game_BTN_Right.visibility = View.VISIBLE
            game_BTN_Left.visibility = View.VISIBLE
        }
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
        game_GRID_Board[index].addView(playerView)

    }

    private fun initEnergyBlastViews() {
        energyBlastViews = Array(game_GRID_Board.size) { index ->
            AppCompatImageView(this).apply {
                setImageResource(R.drawable.energy_blast)
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                visibility = View.GONE
            }.also { img ->
                game_GRID_Board[index].addView(img)
            }
        }
    }

    private fun initSenzuBeansViews() {
        senzuBeansViews = Array(game_GRID_Board.size) { index ->
            AppCompatImageView(this).apply {
                setImageResource(R.drawable.senzu_bean)
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                visibility = View.GONE
            }.also { img ->
                game_GRID_Board[index].addView(img)
            }
        }
    }

    private fun initcoinsViews() {
        coinsViews = Array(game_GRID_Board.size) { index ->
            AppCompatImageView(this).apply {
                setImageResource(R.drawable.ball_01)
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                visibility = View.GONE
            }.also { img ->
                game_GRID_Board[index].addView(img)
            }
        }
    }

    private fun findViews() {
        game_IMG_hearts = arrayOf(
            findViewById(R.id.game_IMG_heart0),
            findViewById(R.id.game_IMG_heart1),
            findViewById(R.id.game_IMG_heart2)
        )
        game_GRID_Board = arrayOf(
            findViewById(R.id.cell_0_0),
            findViewById(R.id.cell_0_1),
            findViewById(R.id.cell_0_2),
            findViewById(R.id.cell_0_3),
            findViewById(R.id.cell_0_4),
            findViewById(R.id.cell_1_0),
            findViewById(R.id.cell_1_1),
            findViewById(R.id.cell_1_2),
            findViewById(R.id.cell_1_3),
            findViewById(R.id.cell_1_4),
            findViewById(R.id.cell_2_0),
            findViewById(R.id.cell_2_1),
            findViewById(R.id.cell_2_2),
            findViewById(R.id.cell_2_3),
            findViewById(R.id.cell_2_4),
            findViewById(R.id.cell_3_0),
            findViewById(R.id.cell_3_1),
            findViewById(R.id.cell_3_2),
            findViewById(R.id.cell_3_3),
            findViewById(R.id.cell_3_4),
            findViewById(R.id.cell_4_0),
            findViewById(R.id.cell_4_1),
            findViewById(R.id.cell_4_2),
            findViewById(R.id.cell_4_3),
            findViewById(R.id.cell_4_4)
        )
        game_BTN_Left = findViewById(R.id.game_BTN_left)
        game_BTN_Right = findViewById(R.id.game_BTN_right)
        game_LAYOUT_screen = findViewById(R.id.game_LAYOUT_screen)
        game_LBL_odometer = findViewById(R.id.game_LBL_odometer)
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

            //set difficulty on button mode
            if (gameDifficulty == "Fast" && tiltMode == false)
               timerDelay = Constants.TimerConstants.HARD_DELAY
            else if (gameDifficulty == "Slow" && tiltMode == false)
                timerDelay =   Constants.TimerConstants.EASY_DELAY

            timer.schedule(object : TimerTask(){
                override fun run() {
                    val gameContinues = gameManager.updateGame()
                    val lostLife = gameManager.prevLives > gameManager.lives
                    val gainLife = gameManager.prevLives < gameManager.lives
                    val gainCoin = gameManager.prevCoins < gameManager.coinsCollected

                    //case lost game
                    if(!gameContinues){
                        stopTimer()
                        gameManager.calcScore()
                        changeToGameOverActivity("Game Over!\nScore:",gameManager.score)
                    }

                    //case lost life
                    if (lostLife) ssp.playSound(R.raw.energy_blast)

                    //case gained life
                    if (gainLife) {
                        gameManager.prevLives = gameManager.lives
                        ssp.playSound(R.raw.senzu_bean)
                    }

                    //case gained coin
                    if (gainCoin) {
                        gameManager.prevCoins = gameManager.coinsCollected
                        ssp.playSound(R.raw.coin)
                    }

                    runOnUiThread {
                        //toast and vibrate on lost life
                        if (lostLife){
                            gameManager.prevLives = gameManager.lives
                            SignalManager
                                .getInstance()
                                .toast(
                                    " Lives Left: "+gameManager.lives.toString(),
                                    SignalManager.ToastLength.SHORT
                                )
                            SignalManager.getInstance().vibrate()

                        }
                        //update player view on death
                        if (gameManager.lives == 0) {
                            playerView.setImageResource(R.drawable.player_dead)
                        }
                        //update odometer and distance every tick
                        gameManager.distance += 1
                        updateOdometer()

                        //update all related game views on board
                        updateSenzuBeansPositionOnBoard()
                        updateCoinsPositionOnBoard()
                        updateEnergyBlastsPositionOnBoard()
                        updateHearts()
                    }

                }
            },0, timerDelay)
        }
    }

    private fun updateOdometer(){
        game_LBL_odometer.text = gameManager.distance.toString()
    }
    private fun updatePlayerPositionOnBoard() {
        val row = gameManager.playerRow
        val col = gameManager.playerCol
        val prevCol= gameManager.playerPrevIndex

        val prevIndex = row * Constants.GameConstants.COLS + prevCol
        val index = row * Constants.GameConstants.COLS + col
        if (prevIndex != index){
            game_GRID_Board[prevIndex].removeView(playerView)
            game_GRID_Board[index].addView(playerView)
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

    private fun updateSenzuBeansPositionOnBoard() {
        val grid = gameManager.grid
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                val index = i * Constants.GameConstants.COLS + j
                if (grid[i][j] == 2) {
                    senzuBeansViews[index].visibility = View.VISIBLE
                }else{
                    senzuBeansViews[index].visibility = View.GONE
                }
            }
        }
    }
    private fun updateCoinsPositionOnBoard() {
        val grid = gameManager.grid
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                val index = i * Constants.GameConstants.COLS + j
                if (grid[i][j] == 3) {
                    coinsViews[index].visibility = View.VISIBLE
                }else{
                    coinsViews[index].visibility = View.GONE
                }
            }
        }
    }

    private fun updateHearts(){
       for(i in game_IMG_hearts.indices){
           if(i < gameManager.lives){
               game_IMG_hearts[i].visibility = View.VISIBLE
           }else{
               game_IMG_hearts[i].visibility = View.GONE
           }
       }
    }

     private fun changeToGameOverActivity(message: String, score: Int) {
        val intent = Intent(this, GameOverActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Constants.BundleKeys.MESSAGE_KEY, message)
        bundle.putInt(Constants.BundleKeys.SCORE_KEY, score)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun setGameSpeed(newDelay: Long) {
        if (timerDelay == newDelay) return
        timerDelay = newDelay

        if (timerOn) {
            stopTimer()
            startTimer()
        }
    }
}