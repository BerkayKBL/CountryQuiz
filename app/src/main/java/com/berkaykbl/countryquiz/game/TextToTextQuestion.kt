package com.berkaykbl.countryquiz.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.berkaykbl.countryquiz.adapter.GameModesAdapter
import com.berkaykbl.countryquiz.databinding.FragmentModesBinding
import com.berkaykbl.countryquiz.databinding.FragmentTextToTextBinding

class TextToTextQuestion() : Fragment() {

    private lateinit var binding: FragmentTextToTextBinding
    private var name = ""
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
        println(binding.answerA.text)
    }

    fun changeName(s: String) {
        binding.answerA.text = s
    }
}