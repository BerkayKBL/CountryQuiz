package com.berkaykbl.countryquiz.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.databinding.ActivityGameBinding
import java.util.Timer

class GameActivity(): AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var categories : ArrayList<String> = ArrayList()
    private val gameUtils : GameUtils = GameUtils()
    private val timer = Timer()
    private val delayMillis = 500L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gameMode = intent.getIntExtra("gameMode", -1)
        categories = intent.getStringArrayListExtra("categories")!!
        categories.remove("all")
        if (gameMode != -1) {
            setGameType(gameMode,25)
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

    /*private fun classicGame(questionCount : Int) {



        binding.questionFragment.removeAllViews()
        binding.questionFragment.addView(LayoutInflater.from(this).inflate(R.layout.fragment_text_to_text, null) as LinearLayout)

        findViewById<FrameLayout>(R.id.questionFragment).addView(R.layout.fragment_text_to_text)
        var category = categories.random()
        category = "capital"
        if (category == "capital" || category == "dialCode" || category == "president") {
            val textToText = TextToTextQuestion()
            supportFragmentManager.beginTransaction()
                .replace(R.id.questionFragment, textToText)
                .disallowAddToBackStack()
                .commit()

            gameUtils.askQuestion(this, askedQuestions, 0, category, Random.nextInt(0, 2), textToText = textToText)

            binding.progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    gameUtils.askQuestion(this@GameActivity, askedQuestions, 0, category, Random.nextInt(0, 2), textToText = textToText)
                }

                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

    }*/

}