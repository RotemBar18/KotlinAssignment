package com.example.first_assignment_game.model

import com.example.first_assignment_game.model.HighScore
import kotlin.apply

data class LeaderBoard(
    val leaderBoard: List<HighScore>

){
    class Builder(
      private val leaderBoard: MutableList<HighScore> = mutableListOf()
    ){
        fun addHighScore(highScore: HighScore ) = apply { (this.leaderBoard ).add(highScore) }
        fun build() = LeaderBoard(leaderBoard)
    }
}
