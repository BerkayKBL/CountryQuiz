package com.berkaykbl.countryquiz.newGame

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.Utils
import com.berkaykbl.countryquiz.adapter.GameModesAdapter
import com.berkaykbl.countryquiz.databinding.FragmentModesBinding

private var lastSelectMode: Int = -1
private var categories: ArrayList<String> = ArrayList()

class GameModeFragment : Fragment() {

    private lateinit var binding: FragmentModesBinding
    private var gameModesList: List<String> = ArrayList()
    private var gameModeAdapter: GameModesAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentModesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lastSelectMode = -1
        val gameModesArray = ArrayList<HashMap<String, HashMap<String, Any>>>()
        gameModesList = resources.getStringArray(R.array.game_modes).toList()
        requireView().findViewById<RecyclerView>(R.id.gameModes).layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        gameModesList.forEach {
            val gameModeHash = HashMap<String, HashMap<String, Any>>()
            val gameModeDetail = HashMap<String, Any>()
            val gameModeNameText = it.split(";")[1]
            val gameModeCategory = it.split(";")[0]
            val gameModeName = resources.getString(
                resources.getIdentifier(
                    "gamemode.$gameModeNameText", "string", requireContext().packageName
                )
            )
            val gameModeDescription = resources.getString(
                resources.getIdentifier(
                    "gamemode.$gameModeNameText.description", "string", requireContext().packageName
                )
            )
            gameModeDetail["name"] = gameModeName
            gameModeDetail["description"] = gameModeDescription
            gameModeDetail["category"] = gameModeCategory
            gameModeDetail["score"] =
                Utils().getDB()!!.bestScores().getCategoryScore(gameModeNameText, categories.joinToString(","))
            gameModeHash[gameModeNameText] = gameModeDetail
            gameModesArray.add(gameModeHash)
        }


        gameModeAdapter = GameModesAdapter(requireContext(), gameModesArray) { p: Int ->
            gameModeCallback(p)
        }
        view.findViewById<RecyclerView>(R.id.gameModes).adapter = gameModeAdapter
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun gameModeCallback(position: Int) {
        if (lastSelectMode != position) {
            binding.gameModes[position].isSelected = true
            if (lastSelectMode != -1) binding.gameModes[lastSelectMode].isSelected = false
            lastSelectMode = position
        } else {
            binding.gameModes[lastSelectMode].isSelected = false
            lastSelectMode = -1
        }
    }

    fun getLastSelectMode(): Int = lastSelectMode

    fun setCategories(newCategories: ArrayList<String>) = run { categories = newCategories }

}