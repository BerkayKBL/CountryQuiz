package com.berkaykbl.countryquiz.newGame

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.Utils
import com.berkaykbl.countryquiz.databinding.ActivityMainBinding
import com.berkaykbl.countryquiz.databinding.ActivityNewGameBinding
import com.berkaykbl.countryquiz.game.GameActivity


class NewGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.new_category_fragment)

        val ar = ArrayList<String>()
        ar.add("capital")
        binding.nextButton.setOnClickListener {
            if (navController.currentDestination!!.label!!.equals("CategoriesFragment")) {

                navController.navigate(R.id.action_GameModeFragment)
                binding.backButton.visibility = View.VISIBLE
                binding.title.text = resources.getString(R.string.gamemode)
                GameModeFragment().setCategories(ar)
            } else if (navController.currentDestination!!.label!!.equals("GameModeFragment")) {
                var gameModeIndex = GameModeFragment().getLastSelectMode()
                gameModeIndex = 5
                var gameMode = Utils().changeGameModeIndex(gameModeIndex)
                var questionCount = -1
                if (gameModeIndex >= 0 && gameModeIndex <= 3) {
                    when(gameModeIndex) {
                        0 -> questionCount = 25
                        1 -> questionCount = 50
                        2 -> questionCount = 100
                        3 -> questionCount = 193
                    }
                }
                if (gameMode != -1) {
                    val bundle = Bundle()
                    bundle.putStringArrayList("categories", ar)
                    bundle.putInt("gameMode", gameMode)
                    bundle.putInt("gameModeIndex", gameModeIndex)
                    Utils().changeActivity(this, GameActivity::class.java, false, bundle)

                }


            }
        }

        binding.backButton.setOnClickListener {
            navController.popBackStack()
            binding.title.text = resources.getString(R.string.categories)
        }


        binding.nextButton.performClick()


    }

    override fun onStart() {
        super.onStart()

        binding.title.text = resources.getString(R.string.categories)
    }
}