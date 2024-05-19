package com.berkaykbl.countryquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.berkaykbl.countryquiz.databinding.ActivityMainBinding
import com.berkaykbl.countryquiz.newGame.NewGameActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils().setDB(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.play.setOnClickListener {
            Utils().changeActivity(this, NewGameActivity()::class.java, true)
        }

        binding.lastMatchs.setOnClickListener {
            Utils().changeActivity(this, LastMatchesActivity()::class.java, true)

        }

    }
}