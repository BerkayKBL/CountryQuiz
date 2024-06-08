package com.berkaykbl.countryquiz

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.berkaykbl.countryquiz.databinding.ActivityMainBinding
import com.berkaykbl.countryquiz.newGame.NewGameActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var currentLocale: String = "en"
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.deleteDatabase("countryquiz_database")
        currentLocale = Locale.getDefault().toLanguageTag()

        Utils().setDB(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.play.setOnClickListener {
            Utils().changeActivity(this, NewGameActivity()::class.java, true)
        }

        binding.lastMatchs.setOnClickListener {
            Utils().changeActivity(this, LastMatchesActivity()::class.java, true)
        }

        currentLocale = Locale.getDefault().toLanguageTag()
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val locale = prefs.getString("app_locale", "")

        if (locale != null) {
            setLocale(locale)
        }


        binding.translation.setOnClickListener {
            when (currentLocale) {
                "en" -> setLocale("tr")
                else -> setLocale("en")
            }
        }

    }

    private fun setLocale(locale: String) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(locale)
        )
        Locale.setDefault(Locale(locale))

        val sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
        sharedPref.putString("app_locale", locale)
        sharedPref.apply()
    }
}