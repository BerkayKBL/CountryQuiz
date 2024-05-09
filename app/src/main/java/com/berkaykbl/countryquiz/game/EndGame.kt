package com.berkaykbl.countryquiz.game

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.berkaykbl.countryquiz.MainActivity
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.Utils
import com.berkaykbl.countryquiz.databinding.ActivityEndGameBinding

class EndGame : AppCompatActivity() {
    private lateinit var binding: ActivityEndGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val winorloose = intent.getIntExtra("winorloose", -1)
        val gameMode = intent.getIntExtra("gameMode", -1)
        val categories = intent.getStringArrayListExtra("categories")
        val score = intent.getIntExtra("score", 0)
        val maxScore = intent.getIntExtra("maxScore", 0)
        val playtime = intent.getIntExtra("playtime", 0)
        if (winorloose == 0) {
            binding.result.text = resources.getString(R.string.win)
            binding.result.setTextColor(
                resources.getColor(
                    R.color.text_win,
                    resources.newTheme()
                )
            )
        } else if (winorloose == 1) {
            binding.result.text = resources.getString(R.string.loose)
            binding.result.setTextColor(
                resources.getColor(
                    R.color.text_loose,
                    resources.newTheme()
                )
            )

            binding.retry.visibility = View.VISIBLE

        } else {
            Utils().changeActivity(this, MainActivity::class.java, false)
        }

        println(categories)
        categories!!.forEach {
            val view = TextView(this)
            view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            view.setTextColor(resources.getColor(R.color.text_primary))
            view.textSize = 25f
            binding.categories.addView(view)
        }


        var gameModeString = ""
        if (gameMode == 0) {
            gameModeString = resources.getString(R.string.gamemode_classic)
        }

        binding.gameMode.text = gameModeString

        binding.playtime.text = changePlaytime(playtime)

        binding.score.text = score.toString()
        binding.maxScore.text = "/$maxScore"


        binding.retry.setOnClickListener {
            val bundle = Bundle()
            bundle.putStringArrayList("categories", categories)
            bundle.putInt("gameMode", gameMode)
            Utils().changeActivity(this, GameActivity::class.java, false, bundle)
        }


        binding.backtomenu.setOnClickListener {
            Utils().changeActivity(this, MainActivity::class.java, false)
        }
    }

    private fun changePlaytime(playtime: Int) : String {
        val minute = playtime / 60
        val seconds = playtime % 60
        return "$minute ${resources.getString(R.string.minute)} $seconds ${resources.getString(R.string.seconds)}"
    }
}