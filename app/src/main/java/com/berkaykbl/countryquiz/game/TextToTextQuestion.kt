package com.berkaykbl.countryquiz.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.databinding.FragmentTextToTextBinding
import org.json.JSONArray

class TextToTextQuestion() : Fragment() {

    private lateinit var binding: FragmentTextToTextBinding
    private var description = ""
    private var title = ""
    private var optionA = ""
    private var optionB = ""
    private var optionC = ""
    private var optionD = ""
    private var optionCallback: (Int) -> Unit = { }
    private var clickedOption: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextToTextBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeViews()

        binding.optionA.setOnClickListener {
            clickedOption(0)
        }

        binding.optionB.setOnClickListener {
            clickedOption(1)
        }

        binding.optionC.setOnClickListener {
            clickedOption(2)
        }

        binding.optionD.setOnClickListener {
            clickedOption(3)
        }
    }

    fun changeQuestion(
        context: Context,
        questionData: JSONArray,
        options: ArrayList<Int>,
        category: String,
        type: Int
    ) {
        clickedOption = false
        val typeString = if (type == 0) "to" else "from"
        description = context.resources.getString(
            context.resources.getIdentifier(
                "question.$typeString.$category",
                "string",
                context.packageName
            )
        )
        if (type == 0) {
            title = questionData.getJSONObject(0).keys().next().toString()

            var i = 0
            options.forEach {
                var optionKey = questionData.getJSONObject(it).keys().next().toString()
                when (i) {
                    0 -> optionA = questionData.getJSONObject(it).getString(optionKey)
                    1 -> optionB = questionData.getJSONObject(it).getString(optionKey)
                    2 -> optionC = questionData.getJSONObject(it).getString(optionKey)
                    3 -> optionD = questionData.getJSONObject(it).getString(optionKey)
                }
                i++
            }
        } else {
            var optionKey = questionData.getJSONObject(0).keys().next().toString()
            title = questionData.getJSONObject(0).getString(optionKey)

            var i = 0
            options.forEach {
                when (i) {
                    0 -> optionA = questionData.getJSONObject(it).keys().next().toString()
                    1 -> optionB = questionData.getJSONObject(it).keys().next().toString()
                    2 -> optionC = questionData.getJSONObject(it).keys().next().toString()
                    3 -> optionD = questionData.getJSONObject(it).keys().next().toString()
                }
                i++
            }
        }
    }


    override fun onStart() {
        super.onStart()
        println(this::binding.isInitialized)
        if (this::binding.isInitialized) {
            changeViews()
        }
    }

    private fun changeViews() {
        binding.description.text = description
        binding.title.text = title
        binding.optionA.text = optionA
        binding.optionB.text = optionB
        binding.optionC.text = optionC
        binding.optionD.text = optionD
    }

    fun setOptionCallback(callback: (Int) -> Unit) {
        optionCallback = callback
    }

    fun checkAnswer(position: Int, correctOption: Int, isCorrect: Boolean) {
        val correctStyle = R.drawable.button_right

        val option = when (position) {
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

    private fun clickedOption(position: Int) {
        if (!clickedOption) {
            optionCallback(position)
            clickedOption = true
        }
    }
}