package com.berkaykbl.countryquiz.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.berkaykbl.countryquiz.R

class GameModesAdapter(
    private val context: Context,
    private val gameModesList: ArrayList<HashMap<String, HashMap<String, String>>>,
    private val callback: (Int, String) -> Unit
) :
    RecyclerView.Adapter<GameModesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gameModeName: TextView = view.findViewById(R.id.gameModeName)
        val gameModeDescription: TextView = view.findViewById(R.id.gameModeDescription)
        val gameModeLayout: LinearLayout = view.findViewById(R.id.gameModeLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.element_gamemode, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gameModesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = gameModesList[position]
        val key = category.keys.toList()[0]
        val value = category[key]
        val name  = value!!.keys.toList()[0]
        val description  = value.values.toList()[0]
        holder.gameModeName.text = name
        holder.gameModeDescription.text = description
        holder.gameModeLayout.setOnClickListener {
            callback(position, key)
        }
    }

}