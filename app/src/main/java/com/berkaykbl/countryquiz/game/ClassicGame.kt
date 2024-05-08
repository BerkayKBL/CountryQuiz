package com.berkaykbl.countryquiz.game

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.berkaykbl.countryquiz.databinding.FragmentClassicGameBinding
import com.berkaykbl.countryquiz.databinding.FragmentTextToTextBinding
import org.json.JSONArray
import kotlin.random.Random

class ClassicGame(private val questionCount: Int, private val categories: ArrayList<String>) :
    Fragment() {
    private lateinit var binding: FragmentClassicGameBinding
    private val askedQuestions : ArrayList<Int> = ArrayList()

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
        var category = categories.random()
        category = "capital"
        if (category == "capital" || category == "dialCode" || category == "president") {
            askQuestion(0, category, Random.nextInt(0, 2))
        }
    }


    private fun askQuestion(categoryType: Int, category: String, type: Int) {
        val question = GameUtils().getQuestion(askedQuestions, category)
        val questionData  = question[0] as JSONArray
        val options = question[1] as ArrayList<Int>
        enableCategoryType(categoryType)
        if (categoryType == 0) {

        }

    }

    private fun enableCategoryType(categoryType: Int) {
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

    fun checkAnswer(clickedOption: Int, options: ArrayList<Int>) {


    }
}