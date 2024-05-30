package com.berkaykbl.countryquiz.newGame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.berkaykbl.countryquiz.databinding.FragmentCategoriesBinding
import com.berkaykbl.countryquiz.databinding.FragmentCustomSettingsBinding


private var lifeCount = 0
private var playtime = 0
private var isEveryQ = false
private var questionCount = 0

class CustomSettingsFragment : Fragment() {
    private lateinit var binding: FragmentCustomSettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.everyQButton.setOnClickListener {
            it.isSelected = !it.isSelected
            isEveryQ = it.isSelected
            binding.playtime.max = if (it.isSelected) {
                 15
            } else {
                1000
            }
        }

        binding.lifeCount.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.lifeCountText.text = progress.toString()
                lifeCount = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.playtime.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.playtimeText.text = progress.toString()
                playtime = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.questionCount.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.questionCountText.text = progress.toString()
                questionCount = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    fun getSettings() : HashMap<String, Any> {
        val settings = HashMap<String, Any>()
        settings["playtime"] = playtime
        settings["lifeCount"] = lifeCount
        settings["isEveryQ"] = isEveryQ
        settings["questionCount"] = questionCount
        return settings
    }

}