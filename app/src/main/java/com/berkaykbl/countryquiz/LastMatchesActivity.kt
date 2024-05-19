package com.berkaykbl.countryquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.berkaykbl.countryquiz.adapter.LastMatchesAdapter
import com.berkaykbl.countryquiz.databinding.ActivityLastMatchesBinding
import com.berkaykbl.countryquiz.databinding.FragmentCategoriesBinding
import com.berkaykbl.countryquiz.game.GameUtils

class LastMatchesActivity: AppCompatActivity() {
    private lateinit var binding : ActivityLastMatchesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLastMatchesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lastMatchesAdapter = LastMatchesAdapter(this, Utils().lastMatchesDao().getAll())
        findViewById<RecyclerView>(R.id.lastMatches).adapter = lastMatchesAdapter
    }
}