package com.berkaykbl.countryquiz.game

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.berkaykbl.countryquiz.MainActivity
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.Utils
import com.berkaykbl.countryquiz.database.BestScoresDao
import com.berkaykbl.countryquiz.database.BestScoresEntity
import com.berkaykbl.countryquiz.database.LastMatchesEntity
import com.berkaykbl.countryquiz.databinding.ActivityEndGameBinding

class EndGame : AppCompatActivity() {
    private lateinit var binding: ActivityEndGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val win = intent.getBooleanExtra("win", false)
        val gameModeIndex = intent.getIntExtra("gameModeIndex", -1)
        val gameMode = intent.getIntExtra("gameMode", -1)
        val categories = intent.getStringArrayListExtra("categories")
        val categoriesString = categories!!.joinToString(",")
        val score = intent.getIntExtra("score", 0)
        val maxScore = intent.getIntExtra("maxScore", 0)
        val playtime = intent.getIntExtra("playtime", 0)
        if (win) {
            binding.result.text = resources.getString(R.string.win)
            binding.result.setTextColor(
                resources.getColor(
                    R.color.text_win,
                    resources.newTheme()
                )
            )
        } else {
            binding.result.text = resources.getString(R.string.loose)
            binding.result.setTextColor(
                resources.getColor(
                    R.color.text_loose,
                    resources.newTheme()
                )
            )

            binding.retry.visibility = View.VISIBLE

        }

        val categoriesView = findViewById<ScrollView>(R.id.categories)
        categoriesView.removeAllViews()
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setHorizontalGravity(Gravity.CENTER)
        categoriesView.addView(linearLayout)
        var i = 0
        categories.forEach { key ->
            val view = TextView(this)
            view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            view.setTextColor(resources.getColor(R.color.text_primary))
            view.textSize = 25f
            view.text = resources.getString(
                resources.getIdentifier(
                    "category.$key",
                    "string",
                    this.packageName
                )
            )
            linearLayout.addView(view)
            i++

        }


        var addScore = false
        var gameModeString = ""
        val gameModeKey = resources.getStringArray(R.array.game_modes)[gameModeIndex]
        if (gameMode == 0) {
            gameModeString = resources.getString(R.string.gamemode_classic)
            binding.maxScore.text = "/$maxScore"

            if (win) {
                addScore = false
            }
        } else {
            addScore = true
            if (gameMode == 1) {
                gameModeString = resources.getString(R.string.gamemode_againsttime)
                binding.maxScore.visibility = View.GONE
            } else if (gameMode == 2) {
                gameModeString = resources.getString(R.string.gamemode_againsttime2)
                binding.maxScore.visibility = View.GONE
            }
        }

        if (addScore) {
            val bestScoresDao = Utils().bestScoresDao()
            val bestScore = getBestScore(bestScoresDao, gameModeKey, categoriesString)

            if (bestScore == null) {
                bestScoresDao.insert(
                    BestScoresEntity(
                        gameModeKey = gameModeKey,
                        gameModeName = gameModeString,
                        time = 111111,
                        categories = categoriesString,
                        score = score,
                        playtime = playtime
                    )
                )
            } else if (bestScore.score < score) {
                val bestScoreCopy = bestScore.copy(score = score)
                bestScoresDao.update(bestScoreCopy)
            }
        }

        binding.gameMode.text = gameModeString

        binding.playtime.text = Utils().changePlaytime(this, playtime)

        binding.score.text = score.toString()

        val lastMatchesDao = Utils().lastMatchesDao()

        lastMatchesDao.insert(
            LastMatchesEntity(
                gameModeKey = gameModeKey,
                gameModeName = gameModeString,
                time = 111111,
                categories = categoriesString,
                win = win,
                score = score,
                playtime = playtime
            )
        )

        binding.retry.setOnClickListener {
            val bundle = Bundle()
            bundle.putStringArrayList("categories", categories)
            bundle.putInt("gameMode", gameMode)
            bundle.putInt("gameModeIndex", gameModeIndex)
            Utils().changeActivity(this, GameActivity::class.java, false, bundle)
        }


        binding.backtomenu.setOnClickListener {
            Utils().changeActivity(this, MainActivity::class.java, false)
        }
    }


    private fun getBestScore(
        bestScoresDao: BestScoresDao,
        gameModeKey: String,
        categoriesString: String
    ): BestScoresEntity? {

        val bestScoresEntity = bestScoresDao.getCategoryScore(gameModeKey, categoriesString)
        if (bestScoresEntity.isNotEmpty()) {
            return bestScoresEntity[0]
        }
        return null
    }
}