package com.berkaykbl.countryquiz.newGame

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
        var gameModeIndex = -1
        var bundle : Bundle = Bundle()
        binding.nextButton.setOnClickListener {
            if (navController.currentDestination!!.label!! == "CategoriesFragment") {

                selectedCategories.clear()
                selectedCategories.addAll(CategoriesFragment().getSelectedCategories())
                if (selectedCategories.isNotEmpty()) {
                    if (selectedCategories.contains("flags")) {
                        if (!checkInternet()) {
                            Toast.makeText(this, resources.getString(R.string.no_internet_connection), Toast.LENGTH_LONG)
                            return@setOnClickListener
                        }
                    }
                    binding.title.text = resources.getString(R.string.gamemode)
                    navController.navigate(R.id.action_GameModeFragment)
                    GameModeFragment().setCategories(selectedCategories)
                }
            } else if (navController.currentDestination!!.label!! == "GameModeFragment") {
                gameModeIndex = GameModeFragment().getLastSelectMode()
                if (gameModeIndex != -1) {
                    val gameModeInfo =
                        resources.getStringArray(R.array.game_modes).toList()[gameModeIndex]
                    bundle.putStringArrayList("categories", selectedCategories)
                    bundle.putString("gameMode", gameModeInfo)
                    bundle.putInt("gameModeIndex", gameModeIndex)
                    if (gameModeInfo.split(";")[0] == "custom") {
                        binding.title.text = resources.getString(R.string.custom_settings)
                        navController.navigate(R.id.action_CustomSettingsFragment)
                    } else {
                        Utils().changeActivity(this, GameActivity::class.java, false, bundle)
                    }

                }
            } else if (navController.currentDestination!!.label!! == "CustomSettingFragment") {
                val settings = CustomSettingsFragment().getSettings()
                val lifeCount = settings["lifeCount"] as Int
                val playtime = settings["playtime"] as Int
                val questionCount = settings["questionCount"] as Int

                if (playtime > 0 && lifeCount > 0 && questionCount > 0) {
                    bundle.putString("playtime", playtime.toString())
                    bundle.putString("lifeCount", lifeCount.toString())
                    bundle.putString("questionCount", questionCount.toString())
                    bundle.putString("isEveryQ", settings["isEveryQ"].toString())
                    Utils().changeActivity(this, GameActivity::class.java, false, bundle)
                }
            }
        }

        binding.backButton.setOnClickListener {
            if (navController.currentDestination!!.label!! == "GameModesFragment") {
                navController.popBackStack()
                binding.title.text = resources.getString(R.string.categories)
            }
            if (navController.currentDestination!!.label!! == "CustomSettingsFragment") {
                navController.popBackStack()
                binding.title.text = resources.getString(R.string.gamemode)
            } else {
                super.onBackPressedDispatcher.onBackPressed()
            }

        }


    }

    override fun onStart() {
        super.onStart()

        binding.title.text = resources.getString(R.string.categories)
    }

    private fun checkInternet(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}