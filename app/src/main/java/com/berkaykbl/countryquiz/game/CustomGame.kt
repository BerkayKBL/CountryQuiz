package com.berkaykbl.countryquiz.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.databinding.FragmentClassicGameBinding
import com.berkaykbl.countryquiz.databinding.FragmentCustomGameBinding
import org.json.JSONArray
import java.util.Timer
import java.util.TimerTask
import kotlin.random.Random

class CustomGame(
    private val gameMode: String,
    private val gameModeIndex: Int,
    private val categories: ArrayList<String>,
    private val settings: HashMap<String, Any>
) : Fragment() {

    private lateinit var binding: FragmentCustomGameBinding
    private val askedQuestions: ArrayList<Int> = ArrayList()
    private var gameUtils: GameUtils = GameUtils()
    private var questionData: JSONArray = JSONArray()
    private var options: ArrayList<Int> = ArrayList()
    private var playtime = 0
    private var totalPlaytime = 0
    private var score: Int = 0
    private var questionCount = 0
    private var isEveryQ = false
    private var isClicked: Boolean = false
    private var categoryType: Int = -1
    private var life: Int = 0
    private var maxLife: Int = 0
    private var playtimeSecond = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomGameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayout = layoutInflater.inflate(R.layout.fragment_question, null)
        binding.fragmentQuestion.addView(linearLayout)
        gameUtils.setCountryData(requireContext())
        askedQuestions.clear()


        playtime = settings["playtime"].toString().toInt()
        playtimeSecond = settings["playtime"].toString().toInt()


        maxLife = settings["lifeCount"].toString().toInt()
        life = settings["lifeCount"].toString().toInt()
        isEveryQ = settings["isEveryQ"].toString().toBoolean()
        questionCount = settings["questionCount"].toString().toInt()

        changeLifes()
        askQuestion()

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    playtime -= 1
                    totalPlaytime++
                    if (playtime == 0) {
                        gameUtils.endGame(
                            requireContext(),
                            false,
                            "custom;$gameMode",
                            gameModeIndex,
                            categories,
                            score,
                            totalPlaytime,
                            questionCount
                        )
                    } else {

                        binding.timeProgress.progress = playtime
                        binding.timeProgressText.text = playtime.toString()
                    }
                }
            }

        }, 1000L, 1000L)


        binding.timeProgress.progress = playtime
        binding.timeProgress.max = 0

        gameUtils.setClicks(view) { it ->
            if (!isClicked) {
                isClicked = true
                gameUtils.checkAnswer(view, activity, it, options) {
                    if (it) {
                        askQuestion()
                    } else {
                        life -= 1
                        askQuestion()
                    }
                }
            }
        }

    }

    private fun askQuestion() {
        isClicked = false
        gameUtils.resetOptions(requireView())
        changeLifes()
        if (score == questionCount) {
            gameUtils.endGame(
                requireContext(),
                true,
                "custom;$gameMode",
                gameModeIndex,
                categories,
                score,
                totalPlaytime,
                questionCount
            )

            return
        }
        if (life == 0) {
            gameUtils.endGame(
                requireContext(),
                false,
                "custom;$gameMode",
                gameModeIndex,
                categories,
                score,
                totalPlaytime,
                questionCount
            )
            return
        }
        var category = categories.random()
        score += 1
        binding.score.text = score.toString()

        if (isEveryQ) {
            playtime = playtimeSecond
        }
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


    private fun changeLifes() {
        var i = 0
        var margin = false
        val lifesView = requireView().findViewById<LinearLayout>(R.id.lifes)
        lifesView.removeAllViews()

        while (i < maxLife) {
            val view = ImageView(requireContext())
            view.adjustViewBounds = true
            view.scaleType = ImageView.ScaleType.FIT_XY
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (i < life) {
                view.setImageDrawable(requireContext().getDrawable(R.drawable.heart))

            } else {
                view.setImageDrawable(requireContext().getDrawable(R.drawable.heart_lost))
            }
            if (!margin) {
                val params = view.layoutParams as ViewGroup.LayoutParams
                val marginParams = ViewGroup.MarginLayoutParams(params)
                marginParams.setMargins(10, 0, 10, 0)
                view.layoutParams = marginParams

            }
            margin = !margin
            i += 1

            lifesView.addView(view)
        }
    }

}