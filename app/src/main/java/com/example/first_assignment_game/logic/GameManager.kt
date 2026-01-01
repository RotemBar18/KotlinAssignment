package com.example.first_assignment_game.logic


import android.util.Log
import com.example.first_assignment_game.R
import com.example.first_assignment_game.utilities.Constants
import com.example.sensorrclass.utilities.SingleSoundPlayer

class GameManager(private val lifeCount: Int = 3) {

    var score: Int = 0
        private set

    var lives: Int = lifeCount
        private set

    var prevLives: Int = lifeCount

    var blastCounter: Int = Constants.GameConstants.BLAST_COUNTER
        private set

    var senzuCounter: Int = 0
        private set

    var coinCounter: Int = 0
        private set

    var coinsCollected: Int = 0
        private set

    var prevCoins: Int = 0

    var playerRow: Int = Constants.PlayerConstants.PLAYER_START_ROW
        private set
    var playerCol: Int = Constants.PlayerConstants.PLAYER_START_COL
        private set

    var playerPrevIndex: Int = 0
        private set
    var isGameRunning: Boolean = false
        private set

    var isHit: Boolean = false
        private set


    var distance: Int = 0
    // 0 = Empty, 1 = obstacle
    val grid: Array<IntArray> = Array(Constants.GameConstants.ROWS) { IntArray(Constants.GameConstants.COLS) }


    fun startGame() {
        isGameRunning = true
        score = 0
        isHit = false
        coinsCollected = 0
        lives = lifeCount
        prevLives = lifeCount
        distance = 0
        playerRow = Constants.PlayerConstants.PLAYER_START_ROW
        playerCol = Constants.PlayerConstants.PLAYER_START_COL
        resetGrid()
    }

    private fun resetGrid() {
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                grid[i][j] = 0
            }
        }
    }

    fun movePlayer(direction: Int): Boolean {
        isHit = false
        // direction: -1 (Left), +1 (Right)
        val newIndex = playerCol + direction
        if (newIndex in 0 until  Constants.GameConstants.COLS) {
            playerPrevIndex = playerCol
            playerCol = newIndex
            if (checkCollision()){
                isHit = true
            }
            return true
        }

        return false
    }

    // Called every 1 second
    fun updateGame(): Boolean {
        isHit =  false
        //Lost Game:
        if(!isGameRunning){
            return false
        }


        // 1. Move obstacles down (Shift grid)
        for (i in Constants.GameConstants.ROWS - 1 downTo 1) {
            for (j in 0 until Constants.GameConstants.COLS) {
                grid[i][j] = grid[i - 1][j]
            }
        }
        if (checkCollision()){
            isHit = true
        }

        // 2. Clear top row
        for (j in 0 until Constants.GameConstants.COLS) {
            grid[0][j] = 0
        }

        // 3. Add new obstacle randomly in top row
        if(blastCounter == Constants.GameConstants.BLAST_COUNTER){
            val randomCol = (0 until Constants.GameConstants.COLS).random()
            grid[0][randomCol] = 1
            blastCounter = 0
        }        else{
            blastCounter++
        }

        // 4. Add new senzu bean randomly in top row
        if(senzuCounter == Constants.GameConstants.SENZU_COUNTER){
            val randomCol = (0 until Constants.GameConstants.COLS).random()
            grid[0][randomCol] = 2
            senzuCounter = 0
        }else {
            senzuCounter++
        }
        // 5. Add new coin randomly in top row
        if(coinCounter == Constants.GameConstants.COIN_COUNTER){
            val randomCol = (0 until Constants.GameConstants.COLS).random()
            grid[0][randomCol] = 3
            coinCounter = 0
        }else {
            coinCounter++
        }
        return true
    }

    private fun checkCollision() : Boolean{
        var playerPosition = grid[playerRow][playerCol]
        //case coin
        if ( playerPosition == Constants.GameConstants.COIN) {
            score += Constants.GameConstants.COIN_SCORE
            prevCoins = coinsCollected
            coinsCollected++
            playerPosition = Constants.GameConstants.EMPTY
            return true
        }
        //case senzu
        if (playerPosition == Constants.GameConstants.SENZU) {
            prevLives = lives
            playerPosition = Constants.GameConstants.EMPTY
            if (lives < lifeCount){
                lives++
            }
            return true
        }
        //case energy blast
        if ( playerPosition ==  Constants.GameConstants.BLAST) {
            prevLives = lives
            lives--
            playerPosition =  Constants.GameConstants.EMPTY

            if (lives <= 0) {
                isGameRunning = false
            }

            return true
        }
        return false
    }

    fun calcScore(){
        val scoreFromCoins = coinsCollected * Constants.GameConstants.COIN_SCORE
        val scoreFromDistance = distance * Constants.GameConstants.SCORE_INCREMENT
        Log.d("scoreFromCoins", scoreFromCoins.toString())
        Log.d("scoreFromDistance", scoreFromDistance.toString())

        score = scoreFromCoins + scoreFromDistance

    }
}



