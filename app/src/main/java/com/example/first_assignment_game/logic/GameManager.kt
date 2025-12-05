package com.example.first_assignment_game.logic


import com.example.first_assignment_game.utilities.Constants

class GameManager(private val lifeCount: Int = 3) {

    var score: Int = 0
        private set

    var lives: Int = lifeCount
        private set

    var prevLives: Int = lifeCount

    var blastcounter: Int = Constants.GameConstants.BLAST_COUNTER
        private set

    var playerRow: Int = Constants.PlayerConstants.PLAYER_START_ROW
        private set
    var playerIndex: Int = 1
        private set

    var playerPrevIndex: Int = 0
        private set
    var isGameRunning: Boolean = false
        private set

    var isHit: Boolean = false
        private set


    // 0 = Empty, 1 = Obstacle
    val grid: Array<IntArray> = Array(Constants.GameConstants.ROWS) { IntArray(Constants.GameConstants.COLS) }


    fun startGame() {
        isGameRunning = true
        score = 0
        lives = lifeCount
        playerIndex = 1
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
        val newIndex = playerIndex + direction
        if (newIndex in 0 until  Constants.GameConstants.COLS) {
            playerPrevIndex = playerIndex
            playerIndex = newIndex
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

        score += Constants.GameConstants.SCORE_INCREMENT


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
        if(blastcounter == Constants.GameConstants.BLAST_COUNTER){
            val randomCol = (0 until Constants.GameConstants.COLS).random()
            grid[0][randomCol] = 1
            blastcounter = 0
        }else{
            blastcounter++
        }


        return true
    }

    private fun checkCollision() : Boolean{
        val playerRow = Constants.GameConstants.ROWS - 1
        
        if (grid[playerRow][playerIndex] == 1) {

            prevLives = lives
            lives--
            grid[playerRow][playerIndex] = 0

            if (lives <= 0) {
                isGameRunning = false
            }

            return true
        }
        return false
    }
}


