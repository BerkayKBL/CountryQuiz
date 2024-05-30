package com.berkaykbl.countryquiz.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.databinding.ActivityGameBinding

class GameActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var categories: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gameMode = intent.getStringExtra("gameMode")!!
        val gameModeIndex = intent.getIntExtra("gameModeIndex", -1)
        val gameModeCategory = gameMode.split(";")[0]
        categories = intent.getStringArrayListExtra("categories")!!
        categories.remove("all")
        val settings = HashMap<String, Any>()
        if (gameModeCategory == "custom") {
            val extras = intent.extras!!
            settings["playtime"] = extras.getString("playtime", "0")
            settings["lifeCount"] = extras.getString("lifeCount", "0")
            settings["isEveryQ"] = extras.getString("isEveryQ", "0")
            settings["questionCount"] = extras.getString("questionCount", "0")
        }
        setGameMode(gameModeCategory, gameMode.split(";")[1], gameModeIndex, settings)
    }

    private fun setGameMode(gameMode: String, gameModeName: String, gameModeIndex: Int, settings: HashMap<String, Any>) {
        if (gameMode == "classic") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.questionFragment, ClassicGame(gameModeName, gameModeIndex, categories))
                .disallowAddToBackStack().commit()
        } else if (gameMode == "againsttime") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.questionFragment, AgainstTime(gameModeName, gameModeIndex, categories))
                .disallowAddToBackStack().commit()
        } else if (gameMode == "custom") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.questionFragment, CustomGame(gameModeName, gameModeIndex, categories, settings))
                .disallowAddToBackStack().commit()
        }
    }


}