package com.example.first_assignment_game.utilities

class Constants {

    object GameConstants {
        const val ROWS = 5
        const val COLS = 3
        const val SCORE_INCREMENT = 7

        const val BLAST_COUNTER = 2

    }

    object PlayerConstants {
        const val PLAYER_START_ROW = 4
        const val PLAYER_START_COL = 1
    }

    object TimerConstants {
        const val DELAY: Long = 500L
    }

    object BundleKeys{
        const val MESSAGE_KEY: String = "MESSAGE_KEY"
        const val SCORE_KEY: String = "SCORE_KEY"

    }
}