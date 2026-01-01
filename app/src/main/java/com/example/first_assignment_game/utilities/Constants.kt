package com.example.first_assignment_game.utilities

class Constants {

    object GameConstants {
        const val ROWS = 5
        const val COLS = 5
        const val SCORE_INCREMENT = 7

        const val EMPTY  = 0
        const val BLAST  = 1
        const val SENZU  = 2

        const val COIN  = 3
        const val BLAST_COUNTER = 2

        const val COIN_COUNTER = 17
        const val COIN_SCORE = 50

        const val SENZU_COUNTER = 31

    }

    object PlayerConstants {
        const val PLAYER_START_ROW = 4
        const val PLAYER_START_COL = 2

    }

    object TimerConstants {
        const val EASY_DELAY: Long = 500L
        const val HARD_DELAY: Long = 250L
    }

    object LeaderBoardConstants{
        const val LEADERBOARD_ROWS = 10

    }

    object TiltConstants{
        const val TILT_THRESH = 2.0
        const val TILT_DELAY = 300
    }
    object BundleKeys{
        const val MESSAGE_KEY: String = "MESSAGE_KEY"
        const val SCORE_KEY: String = "SCORE_KEY"
        const val DIFFICULTY_KEY: String = "DIFFICULTY_KEY"

        const val MODE_TILT_KEY = "MODE_TILT_KEY"


    }



    object SP_Keys{
        const val LEADERBOARD_KEY: String = "LEADERBOARD_KEY"
        const val DATA_FILE: String = "DATA_FILE"

    }

}