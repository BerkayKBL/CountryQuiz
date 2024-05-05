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
    private lateinit var binding : ActivityNewGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController =findNavController(R.id.new_category_fragment)

        binding.nextButton.setOnClickListener {
            if (navController.currentDestination!!.label!!.equals("CategoriesFragment")) {

                navController.navigate(R.id.action_GameModeFragment)
                binding.backButton.visibility = View.VISIBLE
                binding.title.text = resources.getString(R.string.gamemode)
            } else if (navController.currentDestination!!.label!!.equals("GameModeFragment")) {
                val gameMode = GameModeFragment().getLastSelectMode()
                if (gameMode != -1) {
                    val bundle = Bundle()
                    bundle.putStringArrayList("categories", CategoriesFragment().getSelectedCategories())
                    bundle.putInt("gameMode", gameMode)
                    Utils().changeActivity(this, GameActivity::class.java, false, bundle)
                }
            }
        }

        binding.backButton.setOnClickListener {
            navController.popBackStack()
            binding.title.text = resources.getString(R.string.categories)
        }

        binding.nextButton.performClick()
        binding.nextButton.performClick()
    }

    override fun onStart() {
        super.onStart()

        binding.title.text = resources.getString(R.string.categories)
    }
}