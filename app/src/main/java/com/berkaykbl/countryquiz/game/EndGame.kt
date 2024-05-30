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
        val gameMode = intent.getStringExtra("gameMode")
        val categories = intent.getStringArrayListExtra("categories")
        val categoriesString = categories!!.joinToString(",")
        val score = intent.getIntExtra("score", 0)
        val maxScore = intent.getIntExtra("maxScore", 0)
        val playtime = intent.getIntExtra("playtime", 0)
        if (win) {
            binding.result.text = resources.getString(R.string.win)
            binding.result.setTextColor(
                resources.getColor(
                    R.color.text_win, resources.newTheme()
                )
            )
        } else {
            binding.result.text = resources.getString(R.string.loose)
            binding.result.setTextColor(
                resources.getColor(
                    R.color.text_loose, resources.newTheme()
                )
            )

            binding.retry.visibility = View.VISIBLE

        }

        var categoriesText = ""

        categories.forEach { e ->
            val categoryName = resources.getString(
                resources.getIdentifier(
                    "category.$e", "string", this.packageName
                )
            )
            categoriesText += "$categoryName, "
        }

        binding.categories.text = categoriesText


        var addScore = false
        val gameModeKey = resources.getStringArray(R.array.game_modes)[gameModeIndex]

        val gameModeString = resources.getString(
            resources.getIdentifier(
                "gamemode.$gameModeKey", "string", this.packageName
            )
        )
        if (gameMode == "classic") {
            binding.maxScore.text = resources.getString(R.string.max_score, maxScore.toString())

            if (win) {
                addScore = false
            }
        } else if (gameMode == "custom"){
            binding.maxScore.text = resources.getString(R.string.max_score, maxScore.toString())

        } else {
            addScore = true
            if (gameMode == "againsttime") {
                binding.maxScore.visibility = View.GONE
            } else if (gameMode == "againsttime2") {
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
                        time = System.currentTimeMillis().toInt(),
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
                time = System.currentTimeMillis().toInt(),
                categories = categoriesString,
                win = win,
                score = score,
                playtime = playtime
            )
        )

        binding.retry.setOnClickListener {
            val bundle = Bundle()
            bundle.putStringArrayList("categories", categories)
            bundle.putString("gameMode", gameMode)
            bundle.putInt("gameModeIndex", gameModeIndex)
            Utils().changeActivity(this, GameActivity::class.java, false, bundle)
        }


        binding.backtomenu.setOnClickListener {
            Utils().changeActivity(this, MainActivity::class.java, false)
        }
    }


    private fun getBestScore(
        bestScoresDao: BestScoresDao, gameModeKey: String, categoriesString: String
    ): BestScoresEntity? {

        val bestScoresEntity = bestScoresDao.getCategoryScore(gameModeKey, categoriesString)
        if (bestScoresEntity.isNotEmpty()) {
            return bestScoresEntity[0]
        }
        return null
    }
}