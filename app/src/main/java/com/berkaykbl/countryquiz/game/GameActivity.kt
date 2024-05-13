package com.berkaykbl.countryquiz.game

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.databinding.ActivityGameBinding
import java.util.Timer

class GameActivity(): AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var categories : ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gameMode = intent.getIntExtra("gameMode", -1)
        val gameModeIndex = intent.getIntExtra("gameModeIndex", -1)
        categories = intent.getStringArrayListExtra("categories")!!
        categories.remove("all")
        if (gameMode != -1) {
            setGameMode(gameMode,gameModeIndex)
        }


    }

    private fun setGameMode(gameMode: Int, gameModeIndex: Int) {
        if (gameMode == 0) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.questionFragment, ClassicGame(gameMode, gameModeIndex, categories))
                .disallowAddToBackStack()
                .commit()
        } else if (gameMode == 1 || gameMode == 2) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.questionFragment, AgainstTime(gameMode, gameModeIndex, categories))
                .disallowAddToBackStack()
                .commit()
        }
    }


}