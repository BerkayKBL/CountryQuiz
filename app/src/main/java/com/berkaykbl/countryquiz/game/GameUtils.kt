package com.berkaykbl.countryquiz.game

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.Utils
import com.berkaykbl.countryquiz.databinding.FragmentQuestionBinding
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.util.Collections
import java.util.Timer
import java.util.TimerTask
import kotlin.random.Random

class GameUtils {

    private var countryData: JSONArray = JSONArray()
    private var fragmentType: Int = 0

    fun setCountryData(context: Context) {
        var countryJSONData: String? = null
        try {
            val inputStream = context.assets.open("countryData.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            countryJSONData = String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        countryData = JSONArray(countryJSONData)
    }

    fun getCountryData(): JSONArray {
        return countryData
    }

    fun getQuestion(askedQuestions: ArrayList<Int>, category: String): ArrayList<Any> {
        val options = JSONArray()
        val optionIDS: ArrayList<Int> = ArrayList()
        val optionCodes: ArrayList<String> = ArrayList()
        var correctOptionID : Int = 0

        var i = 0
        while (options.length() < 4) {
            val countryId = Random.nextInt(0, 5)
            val countryData = getCountryData().getJSONObject(countryId)
            val countryCode: String = countryData.getString("countryCode")
            if (!askedQuestions.contains(countryId) && !optionCodes.contains(countryCode)) {

                if (i == 0) {
                    correctOptionID = countryId
                }

                if (category != "flags") {
                    optionIDS.add(i)
                    options.put(
                        JSONObject().put(
                            countryData.getString("name"),
                            countryData.getString(category)
                        )
                    )
                } else {
                    optionIDS.add(i)
                    options.put(
                        JSONObject().put(
                            countryData.getString("name"),
                            "https://flagcdn.com/h240/$countryCode.jpg"
                        )
                    )
                }

                optionCodes.add(countryCode)
                i++
            }
        }

        var callback = ArrayList<Any>()
        optionIDS.shuffle()
        callback.add(options)
        callback.add(optionIDS)
        callback.add(correctOptionID)

        return callback

    }


    fun endGame(
        context: Context,
        win: Boolean,
        gameMode: Int,
        gameModeIndex: Int,
        categories: ArrayList<String>,
        score: Int,
        playtime: Int,
        maxScore: Int = 0
    ) {
        val bundle = Bundle()
        bundle.putBoolean("win", win)
        bundle.putInt("gameModeIndex", gameModeIndex)
        bundle.putInt("gameMode", gameMode)
        bundle.putStringArrayList("categories", categories)
        bundle.putInt("score", score)
        bundle.putInt("maxScore", maxScore)
        bundle.putInt("playtime", playtime)
        Utils().changeActivity(context, EndGame::class.java, false, bundle)
    }

    fun getTextButtons(view: View): ArrayList<Button> {
        val buttons = ArrayList<Button>()

        buttons.add(view.findViewById(R.id.optionA))
        buttons.add(view.findViewById(R.id.optionB))
        buttons.add(view.findViewById(R.id.optionC))
        buttons.add(view.findViewById(R.id.optionD))

        return buttons
    }

    fun getImageButtons(view: View): ArrayList<AppCompatImageView> {
        val buttons = ArrayList<AppCompatImageView>()

        buttons.add(view.findViewById(R.id.imageOptionA))
        buttons.add(view.findViewById(R.id.imageOptionB))
        buttons.add(view.findViewById(R.id.imageOptionC))
        buttons.add(view.findViewById(R.id.imageOptionD))

        return buttons
    }

    fun setClicks(view: View, callback: (Int) -> Unit) {


        var i = 0
        val textButtons = getTextButtons(view)
        textButtons.forEach {it ->
            it.setOnClickListener { callback(textButtons.indexOf(it)) }
            i++
        }

        i = 0

        val imageButton = getImageButtons(view)
        imageButton.forEach { it ->
            it.setOnClickListener {  callback(imageButton.indexOf(it)) }
            i++
        }
    }

    fun resetOptions(view: View) {
        var i = 0
        val textButtons = getTextButtons(view)
        textButtons.forEach {
            it.setBackgroundResource(R.drawable.button_style)
            i++
        }

        i = 0

        val imageButton = getImageButtons(view)
        imageButton.forEach {
            it.setBackgroundResource(0)
            i++
        }
    }


    fun checkAnswer(
        view: View,
        activity: FragmentActivity?,
        clickedOption: Int,
        options: ArrayList<Int>,
        categoryType: Int,
        callback: (Boolean) -> Unit
    ) {
        val correctStyle = R.drawable.button_right

        val correctOption = options.indexOf(0)
        val isCorrect = correctOption == clickedOption
        println(clickedOption)
        val option = getTextButtons(view)[clickedOption]
        val optionImage = getImageButtons(view)[clickedOption]

        if (isCorrect) {
            option.setBackgroundResource(correctStyle)
            optionImage.setBackgroundResource(correctStyle)
        } else {
            val wrongStyle = R.drawable.button_wrong
            val correctOptionView = getTextButtons(view)[correctOption]
            val correctOptionImageView = getImageButtons(view)[correctOption]

            optionImage.setBackgroundResource(wrongStyle)
            option.setBackgroundResource(wrongStyle)
            correctOptionView.setBackgroundResource(correctStyle)
            correctOptionImageView.setBackgroundResource(correctStyle)

        }


        val timer = Timer()

        timer.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    callback(isCorrect)
                }
            }

        }, 500L)
    }


    fun enableCategoryType(view: View, categoryType: Int, type: Int) {

        if (categoryType == 0) {
            view.findViewById<LinearLayout>(R.id.textOptionLayout).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.title).visibility = View.VISIBLE


            view.findViewById<GridLayout>(R.id.imageOptionLayout).visibility = View.GONE
            view.findViewById<ImageView>(R.id.questionImage).visibility = View.GONE
        } else {
            if (type == 0) {
                view.findViewById<GridLayout>(R.id.imageOptionLayout).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.title).visibility = View.VISIBLE

                view.findViewById<LinearLayout>(R.id.textOptionLayout).visibility = View.GONE
                view.findViewById<ImageView>(R.id.questionImage).visibility = View.GONE
            } else {
                view.findViewById<LinearLayout>(R.id.textOptionLayout).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.questionImage).visibility = View.VISIBLE

                view.findViewById<GridLayout>(R.id.imageOptionLayout).visibility = View.GONE
                view.findViewById<TextView>(R.id.title).visibility = View.GONE

            }
        }
    }

    fun changeQuestion(context: Context, view: View, description: String, categoryType: Int, type: Int, questionData: JSONArray, options: ArrayList<Int>) {
        enableCategoryType(view, categoryType, type)
        view.findViewById<TextView>(R.id.description).text = description
        if (categoryType == 0) {
            if (type == 0) {
                view.findViewById<TextView>(R.id.title).text = questionData.getJSONObject(0).keys().next().toString()

                var i = 0
                options.forEach {
                    var optionKey = questionData.getJSONObject(it).keys().next().toString()

                    val optionButton = getTextButtons(view)[i]
                    optionButton.text = questionData.getJSONObject(it).getString(optionKey)
                    i++
                }
            } else {
                var optionKey = questionData.getJSONObject(0).keys().next().toString()
                view.findViewById<TextView>(R.id.title).text = questionData.getJSONObject(0).getString(optionKey)

                var i = 0

                options.forEach {

                    val optionButton = getTextButtons(view)[i]
                    optionButton.text = questionData.getJSONObject(it).keys().next().toString()
                    i++
                }
            }
        } else {
            if (type == 0) {
                view.findViewById<TextView>(R.id.title).text = questionData.getJSONObject(0).keys().next().toString()
                var i = 0
                options.forEach {
                    var optionKey = questionData.getJSONObject(it).keys().next().toString()

                    val imageButton = getImageButtons(view)[it]
                    Glide.with(context)
                        .load(questionData.getJSONObject(it).getString(optionKey))
                        .into(imageButton)
                    i++
                }
            } else {
                var optionKey = questionData.getJSONObject(0).keys().next().toString()
                Glide.with(context)
                    .load(questionData.getJSONObject(0).getString(optionKey))
                    .into(view.findViewById(R.id.questionImage))

                var i = 0
                options.forEach {

                    val optionButton = getTextButtons(view)[i]
                    optionButton.text = questionData.getJSONObject(it).keys().next().toString()
                    i++
                }
            }
        }
    }


}