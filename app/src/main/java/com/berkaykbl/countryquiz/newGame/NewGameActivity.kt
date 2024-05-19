package com.berkaykbl.countryquiz.newGame

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.Utils
import com.berkaykbl.countryquiz.databinding.ActivityNewGameBinding
import com.berkaykbl.countryquiz.game.GameActivity


class NewGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.new_category_fragment)

        val selectedCategories = ArrayList<String>()
        binding.nextButton.setOnClickListener {
            if (navController.currentDestination!!.label!! == "CategoriesFragment") {

                navController.navigate(R.id.action_GameModeFragment)
                binding.backButton.visibility = View.VISIBLE
                binding.title.text = resources.getString(R.string.gamemode)
                selectedCategories.addAll(CategoriesFragment().getSelectedCategories())
                GameModeFragment().setCategories(selectedCategories)
            } else if (navController.currentDestination!!.label!! == "GameModeFragment") {
                var gameModeIndex = GameModeFragment().getLastSelectMode()
                var gameMode = Utils().changeGameModeIndex(gameModeIndex)
                if (gameMode != -1) {
                    val bundle = Bundle()
                    bundle.putStringArrayList("categories", selectedCategories)
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


    }

    override fun onStart() {
        super.onStart()

        binding.title.text = resources.getString(R.string.categories)
    }
}