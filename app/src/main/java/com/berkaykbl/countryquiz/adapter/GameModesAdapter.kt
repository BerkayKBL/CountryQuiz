package com.berkaykbl.countryquiz.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.database.BestScoresEntity

class GameModesAdapter(
    private val context: Context,
    private val gameModesList: ArrayList<HashMap<String, HashMap<String, Any>>>,
    private val callback: (Int) -> Unit
) : RecyclerView.Adapter<GameModesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gameModeName: TextView = view.findViewById(R.id.gameModeName)
        val gameModeDescription: TextView = view.findViewById(R.id.gameModeDescription)
        val gameModeLayout: LinearLayout = view.findViewById(R.id.gameModeLayout)
        val gameModeScore: TextView = view.findViewById(R.id.gameModeScore)
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
        val name = value!!["name"]
        val description = value["description"]
        val categoryName = value["category"]
        val score = value["score"] as ArrayList<BestScoresEntity>
        holder.gameModeName.text = name.toString()
        holder.gameModeDescription.text = description.toString()
        if (score.isNotEmpty()) {
            val scoresEntity = score[0]
            if (categoryName == "againsttime") {
                holder.gameModeScore.text =
                    context.resources.getString(R.string.best_score, scoresEntity.score.toString())
                holder.gameModeScore.visibility = View.VISIBLE
            } else if (categoryName == "classic") {
                holder.gameModeScore.text = context.resources.getString(
                    R.string.best_score, scoresEntity.playtime.toString()
                )
                holder.gameModeScore.visibility = View.VISIBLE
            }
        }

        holder.gameModeLayout.setOnClickListener {
            callback(position)
        }
    }

}