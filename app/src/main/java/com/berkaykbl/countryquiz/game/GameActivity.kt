package com.berkaykbl.countryquiz.game

import android.os.Bundle
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
        val questionCount = intent.getIntExtra("questionCount", -1)
        categories = intent.getStringArrayListExtra("categories")!!
        categories.remove("all")
        if (gameMode != -1) {
            setGameType(gameMode,questionCount)
        }


    }

    private fun setGameType(gameType: Int, questionCount: Int) {
        if (gameType == 0) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.questionFragment, ClassicGame(questionCount, categories))
                .disallowAddToBackStack()
                .commit()
        }
    }


}