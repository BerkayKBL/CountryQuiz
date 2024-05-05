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
import com.berkaykbl.countryquiz.adapter.CategoriesAdapter
import com.berkaykbl.countryquiz.adapter.GameModesAdapter
import com.berkaykbl.countryquiz.databinding.FragmentCategoriesBinding
import com.berkaykbl.countryquiz.databinding.FragmentModesBinding

private var lastSelectMode : Int = -1
class GameModeFragment : Fragment() {

    private lateinit var binding: FragmentModesBinding
    private var gameModesList: List<String> = ArrayList()
    private var gameModeAdapter: GameModesAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lastSelectMode = -1
        val gameModesArray = ArrayList<HashMap<String, HashMap<String, String>>>()
        gameModesList = resources.getStringArray(R.array.game_modes).toList()
        requireView().findViewById<RecyclerView>(R.id.gameModes).layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        gameModesList.forEach {
            val gameModeHash = HashMap<String, HashMap<String, String>>()
            val gameModeDetail = HashMap<String, String>()
            val gameModeName = resources.getString(
                resources.getIdentifier(
                    "gamemode.$it",
                    "string",
                    requireContext().packageName
                )
            )
            val gameModeDescription = resources.getString(
                resources.getIdentifier(
                    "gamemode.$it.description",
                    "string",
                    requireContext().packageName
                )
            )
            gameModeDetail.put(gameModeName, gameModeDescription)
            gameModeHash.put(it, gameModeDetail)
            gameModesArray.add(gameModeHash)
        }


        gameModeAdapter =
            GameModesAdapter(requireContext(), gameModesArray) { p: Int, key: String ->
                gameModeCallback(p, key)
            }
        view.findViewById<RecyclerView>(R.id.gameModes).adapter = gameModeAdapter
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun gameModeCallback(position: Int, key: String){
        if (lastSelectMode != position) {
            binding.gameModes[position].isSelected = true
            if (lastSelectMode != -1) binding.gameModes[lastSelectMode].isSelected = false
            lastSelectMode = position
        } else {
            binding.gameModes[lastSelectMode].isSelected = false
            lastSelectMode = -1
        }
    }

    fun getLastSelectMode() : Int = lastSelectMode

    override fun onStart() {
        super.onStart()

    }
}