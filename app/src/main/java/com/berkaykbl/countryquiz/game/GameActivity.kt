package com.berkaykbl.countryquiz.game

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.databinding.ActivityGameBinding

class GameActivity(private var name: String): AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var categories : ArrayList<String> = ArrayList()
    private var askedQuestions : ArrayList<Int> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gameMode = intent.getIntExtra("gameMode", -1)
        categories = intent.getStringArrayListExtra("categories")!!

        if (gameMode != -1) {
            when(gameMode) {
                0 -> classicGame(25)
                1 -> classicGame(50)
                2 -> classicGame(100)
                3 -> classicGame(193)
                else -> classicGame(193)

            }
        }
    }

    fun classicGame(questionCount : Int) {
        categories.remove("all")

        var category = categories.random()
        category = "capital"
        if (category == "capital" || category == "dialCode") {
            val textToText = TextToTextQuestion()
            supportFragmentManager.beginTransaction()
                .replace(R.id.questionFragment, textToText)
                .commit()

            getQuestion(15, "country")
            askQuestion(textToText)
        }
    }

    fun askQuestion(fragment: Fragment) {

    }

    fun getQuestion(position: Int, category: String) {
        val r = resources.getXml(R.xml.countries)
    }
}