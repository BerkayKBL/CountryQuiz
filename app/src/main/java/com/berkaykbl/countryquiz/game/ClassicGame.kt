package com.berkaykbl.countryquiz.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.databinding.FragmentClassicGameBinding
import org.json.JSONArray
import java.util.Timer
import java.util.TimerTask
import kotlin.random.Random

class ClassicGame(
    private val gameMode: Int,
    private val gameModeIndex: Int,
    private val categories: ArrayList<String>
) : Fragment() {
    private lateinit var binding: FragmentClassicGameBinding
    private val askedQuestions: ArrayList<Int> = ArrayList()
    private var questionData: JSONArray = JSONArray()
    private var options: ArrayList<Int> = ArrayList()
    private var gameUtils: GameUtils = GameUtils()
    private var categoryType: Int = -1
    private var isClicked: Boolean = false
    private var currentQuestion: Int = 0
    private var playtime = 0
    private var questionCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassicGameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayout = layoutInflater.inflate(R.layout.fragment_question, null)
        binding.fragmentQuestion.addView(linearLayout)

        gameUtils.setCountryData(requireContext())


        when (gameModeIndex) {
            0 -> questionCount = 25
            1 -> questionCount = 50
            2 -> questionCount = 100
            3 -> questionCount = 193
        }

        askQuestion()


        binding.progress.setOnTouchListener { _, _ -> true }

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                playtime += 1
            }

        }, 1000L, 1000L)


        binding.questionCount.text = questionCount.toString()
        binding.currentQuestion.text = currentQuestion.toString()
        binding.progress.progress = currentQuestion
        binding.progress.max = questionCount

        gameUtils.setClicks(view) { it ->
            if (!isClicked) {
                isClicked = true
                gameUtils.checkAnswer(view, activity, it, options) {
                    if (it) {
                        askQuestion()
                    } else {
                        gameUtils.endGame(
                            requireContext(),
                            false,
                            gameMode,
                            gameModeIndex,
                            categories,
                            currentQuestion,
                            playtime,
                            questionCount
                        )
                    }
                }
            }
        }
    }

    private fun askQuestion() {
        isClicked = false
        gameUtils.resetOptions(requireView())
        if (questionCount == currentQuestion) {
            gameUtils.endGame(
                requireContext(),
                true,
                gameMode,
                gameModeIndex,
                categories,
                currentQuestion,
                playtime,
                questionCount
            )
            return
        }
        var category = categories.random()
        currentQuestion += 1
        binding.currentQuestion.text = currentQuestion.toString()
        binding.progress.progress = currentQuestion
        val type = Random.nextInt(0, 2)
        if (category == "capital" || category == "dialCode" || category == "president") {
            categoryType = 0
        } else if (category == "flags") {
            categoryType = 1
        }
        val question = gameUtils.getQuestion(askedQuestions, category)
        questionData = question[0] as JSONArray
        options = question[1] as ArrayList<Int>
        val correctOption = question[2] as Int
        askedQuestions.add(correctOption)
        val typeString = if (type == 0) "to" else "from"
        val description = requireContext().resources.getString(
            requireContext().resources.getIdentifier(
                "question.$typeString.$category", "string", requireContext().packageName
            )
        )

        if (questionData.length() == 5) {
            askQuestion()
            return
        }
        gameUtils.changeQuestion(
            requireContext(), requireView(), description, categoryType, type, questionData, options
        )

    }


}