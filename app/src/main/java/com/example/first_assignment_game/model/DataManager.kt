package com.example.first_assignment_game.model

import com.example.first_assignment_game.R

class DataManager  {
    companion object {
        private val backgrounds = arrayOf(
            R.drawable.background_01,
            R.drawable.background_02,
            R.drawable.background_03,
            R.drawable.background_04,
            R.drawable.background_05,
            R.drawable.background_06,
        )

        fun getRandomBackground(): Int {
            return backgrounds.random()
        }


    }
}