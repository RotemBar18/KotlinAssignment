package com.example.first_assignment_game.ui

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.first_assignment_game.R
import com.example.first_assignment_game.interfaces.Callback_HighScoreClicked
import com.example.first_assignment_game.model.HighScore
import com.example.first_assignment_game.model.LeaderBoard
import com.example.first_assignment_game.utilities.Constants
import com.example.first_assignment_game.utilities.SharedPreferencesManager
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson


class LeaderBoardFragment : Fragment() {


    private lateinit var highscore_LAYOUT_highscore_list: LinearLayoutCompat




    var highScoreItemClicked: Callback_HighScoreClicked? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(
            R.layout.fragment_leaderboard,
            container,
            false)
        findViews(v)
        initViews(v)
        return v
    }

    private fun initViews(v: View) {
        initLeaderboard()

    }


    private fun initLeaderboard() {
        val gson = Gson()

        val leaderBoardJson: String =
            SharedPreferencesManager
                .getInstance()
                .getString(
                    Constants.SP_Keys.LEADERBOARD_KEY,
                    ""
                )

        val leaderBoard : LeaderBoard = if (leaderBoardJson.isNotEmpty())
            try {
                gson.fromJson(
                    leaderBoardJson,
                    LeaderBoard::class.java
                )
            } catch(e: Exception) {
                LeaderBoard(mutableListOf())
            }else  {
                LeaderBoard(mutableListOf())

            }
        val sortedLeaderBoard = leaderBoard.leaderBoard.sortedByDescending { it.score }
        loadHighScoresUI(sortedLeaderBoard)
    }
    private fun loadHighScoresUI(leaderBoard: List<HighScore>) {
        highscore_LAYOUT_highscore_list.removeAllViews()
        if (leaderBoard.isEmpty()) {
            // case there are no high scores
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(12, 12, 12, 12)
                val textView = MaterialTextView(requireContext()).apply {
                    text = "No scores yet"
                    textSize = 12f
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                }
                addView(textView)
            }
            highscore_LAYOUT_highscore_list.addView(row)
            return
        }

        // case there are high scores
        for (highScore in leaderBoard.take(10)) {
            //create a linear layout for each row
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(4, 2, 4, 2)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )



                //create view for each highscore column (name, score, lat, lon)
                val nameLBL = MaterialTextView(requireContext()).apply {
                    maxLines = 1
                    text = highScore.name
                    textSize = resources.getDimension(R.dimen.highscore_text_size)
                    gravity = Gravity.START
                    layoutParams = LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        4f)
                }
                val scoreLBL = MaterialTextView(requireContext()).apply {
                    text = highScore.score.toString()
                    gravity = Gravity.END
                    textSize = resources.getDimension(R.dimen.highscore_text_size)
                    layoutParams = LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f)
                }
                val latLBL = MaterialTextView(requireContext()).apply {
                    text = String.format("%.2f", highScore.lat)
                    textSize = resources.getDimension(R.dimen.highscore_text_size)
                    gravity = Gravity.END
                    layoutParams = LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f)
                }
                val lonLBL = MaterialTextView(requireContext()).apply {
                    text = String.format("%.2f", highScore.lon)
                    textSize = resources.getDimension(R.dimen.highscore_text_size)
                    gravity = Gravity.END
                    layoutParams = LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f)
                }
                val pinImg = MaterialTextView(requireContext()).apply {
                    text = "📍"
                    textSize = resources.getDimension(R.dimen.highscore_text_size)
                    gravity = Gravity.END
                    layoutParams = LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f)
                }
                addView(nameLBL)
                addView(scoreLBL)
                addView(latLBL)
                addView(lonLBL)
                addView(pinImg)
            }
            row.setOnClickListener {
                highScoreItemClicked?.HighScoreItemClicked(highScore.lat, highScore.lon)
            }
            highscore_LAYOUT_highscore_list.addView(row)
        }
    }

    private fun findViews(view: View) {
        highscore_LAYOUT_highscore_list = view.findViewById(R.id.highscore_LAYOUT_highscore_list)

    }
}


