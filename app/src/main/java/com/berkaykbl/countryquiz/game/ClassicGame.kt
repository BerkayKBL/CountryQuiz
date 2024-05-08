package com.berkaykbl.countryquiz.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.Utils
import com.berkaykbl.countryquiz.databinding.FragmentClassicGameBinding
import org.json.JSONArray
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timer
import kotlin.random.Random

class ClassicGame(private val questionCount: Int, private val categories: ArrayList<String>) :
    Fragment() {
    private lateinit var binding: FragmentClassicGameBinding
    private val askedQuestions: ArrayList<Int> = ArrayList()
    private var questionData: JSONArray = JSONArray()
    private var options: ArrayList<Int> = ArrayList()
    private var gameUtils: GameUtils = GameUtils()
    private var categoryType: Int = -1
    private var isClicked: Boolean = false
    private var currentQuestion: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassicGameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameUtils.setCountryData(requireContext())
        askQuestion()


        binding.progress.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }
        })


        binding.questionCount.text = questionCount.toString()
        binding.currentQuestion.text = currentQuestion.toString()
        binding.progress.progress = currentQuestion
        binding.progress.max = questionCount
        binding.optionA.setOnClickListener { checkAnswer(0) }
        binding.optionB.setOnClickListener { checkAnswer(1) }
        binding.optionC.setOnClickListener { checkAnswer(2) }
        binding.optionD.setOnClickListener { checkAnswer(3) }
        binding.imageOptionA.setOnClickListener { checkAnswer(0) }
        binding.imageOptionB.setOnClickListener { checkAnswer(1) }
        binding.imageOptionC.setOnClickListener { checkAnswer(2) }
        binding.imageOptionD.setOnClickListener { checkAnswer(3) }

    }


    private fun askQuestion() {
        isClicked = false
        resetOptions()
        var category = categories.random()
        currentQuestion += 1
        binding.currentQuestion.text = currentQuestion.toString()
        binding.progress.progress = currentQuestion
        val type = Random.nextInt(0, 2)
        if (category == "capital" || category == "dialCode" || category == "president") {
            categoryType = 0
            val question = gameUtils.getQuestion(askedQuestions, category)
            questionData = question[0] as JSONArray
            options = question[1] as ArrayList<Int>
            val typeString = if (type == 0) "to" else "from"
            val description = requireContext().resources.getString(
                requireContext().resources.getIdentifier(
                    "question.$typeString.$category",
                    "string",
                    requireContext().packageName
                )
            )

            changeQuestion(description, type)
        }

    }

    private fun enableCategoryType() {
        if (categoryType == 0) {
            binding.textOptionLayout.visibility = View.VISIBLE
            binding.title.visibility = View.VISIBLE


            binding.imageOptionLayout.visibility = View.GONE
            binding.questionImage.visibility = View.GONE
        } else {
            binding.imageOptionLayout.visibility = View.VISIBLE
            binding.questionImage.visibility = View.VISIBLE

            binding.textOptionLayout.visibility = View.GONE
            binding.title.visibility = View.GONE
        }
    }


    private fun changeQuestion(description: String, type: Int) {
        enableCategoryType()
        binding.description.text = description
        if (categoryType == 0) {
            if (type == 0) {
                binding.title.text = questionData.getJSONObject(0).keys().next().toString()

                var i = 0
                options.forEach {
                    var optionKey = questionData.getJSONObject(it).keys().next().toString()
                    when (i) {
                        0 -> binding.optionA.text =
                            questionData.getJSONObject(it).getString(optionKey)

                        1 -> binding.optionB.text =
                            questionData.getJSONObject(it).getString(optionKey)

                        2 -> binding.optionC.text =
                            questionData.getJSONObject(it).getString(optionKey)

                        3 -> binding.optionD.text =
                            questionData.getJSONObject(it).getString(optionKey)
                    }
                    i++
                }
            } else {
                var optionKey = questionData.getJSONObject(0).keys().next().toString()
                binding.title.text = questionData.getJSONObject(0).getString(optionKey)

                var i = 0
                options.forEach {
                    when (i) {
                        0 -> binding.optionA.text =
                            questionData.getJSONObject(it).keys().next().toString()

                        1 -> binding.optionB.text =
                            questionData.getJSONObject(it).keys().next().toString()

                        2 -> binding.optionC.text =
                            questionData.getJSONObject(it).keys().next().toString()

                        3 -> binding.optionD.text =
                            questionData.getJSONObject(it).keys().next().toString()
                    }
                    i++
                }
            }
        }
    }


    private fun resetOptions() {
        binding.imageOptionA.setBackgroundResource(0)
        binding.imageOptionB.setBackgroundResource(0)
        binding.imageOptionC.setBackgroundResource(0)
        binding.imageOptionD.setBackgroundResource(0)

        binding.optionA.setBackgroundResource(R.drawable.button_style)
        binding.optionB.setBackgroundResource(R.drawable.button_style)
        binding.optionC.setBackgroundResource(R.drawable.button_style)
        binding.optionD.setBackgroundResource(R.drawable.button_style)
    }

    private fun checkAnswer(clickedOption: Int) {
        if (isClicked) return
        isClicked = true
        val correctStyle = R.drawable.button_right

        val correctOption = options.indexOf(0)
        val isCorrect = correctOption == clickedOption
        if (categoryType == 0) {

            val option = when (clickedOption) {
                0 -> binding.optionA
                1 -> binding.optionB
                2 -> binding.optionC
                3 -> binding.optionD
                else -> binding.optionA
            }

            if (isCorrect) {
                option.setBackgroundResource(correctStyle)
            } else {
                val wrongStyle = R.drawable.button_wrong
                val correctOptionView = when (correctOption) {
                    0 -> binding.optionA
                    1 -> binding.optionB
                    2 -> binding.optionC
                    3 -> binding.optionD
                    else -> binding.optionA
                }

                option.setBackgroundResource(wrongStyle)
                correctOptionView.setBackgroundResource(correctStyle)
            }

        }


        val timer = Timer()

        timer.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    if (isCorrect) {
                        askQuestion()
                    } else {
                    }
                }
            }

        }, 500L)
    }
}