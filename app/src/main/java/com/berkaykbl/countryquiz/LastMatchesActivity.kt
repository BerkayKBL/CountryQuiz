package com.berkaykbl.countryquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkaykbl.countryquiz.adapter.LastMatchesAdapter
import com.berkaykbl.countryquiz.databinding.ActivityLastMatchesBinding

class LastMatchesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLastMatchesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLastMatchesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lastMatches.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val lastMatchesDao = Utils().lastMatchesDao()
        val lastMatches = lastMatchesDao.getAll()
        val lastMatchesAdapter = LastMatchesAdapter(this, lastMatches)
        binding.lastMatches.adapter = lastMatchesAdapter
    }
}