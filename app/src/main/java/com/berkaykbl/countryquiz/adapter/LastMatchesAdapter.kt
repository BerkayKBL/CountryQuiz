package com.berkaykbl.countryquiz.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.Utils
import com.berkaykbl.countryquiz.database.LastMatchesEntity
import com.berkaykbl.countryquiz.game.GameUtils

class LastMatchesAdapter(
    private val context: Context,
    private val lastMatches: List<LastMatchesEntity>
) :
    RecyclerView.Adapter<LastMatchesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gameMode: TextView = view.findViewById(R.id.gameMode)
        val win: TextView = view.findViewById(R.id.win)
        val questionCountText: TextView = view.findViewById(R.id.questionCountText)
        val questionCount: TextView = view.findViewById(R.id.questionCount)
        val playtime: TextView = view.findViewById(R.id.playtime)
        val score: TextView = view.findViewById(R.id.score)
        val categories: TextView = view.findViewById(R.id.categories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.element_last_match, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lastMatches.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lastMatch = lastMatches[position]

        holder.gameMode.text = lastMatch.gameModeName
        var categoriesText = ""

        lastMatch.categories.split(",").forEach { e ->
            val categoryName = context.resources.getString(
                context.resources.getIdentifier(
                    "category.$e",
                    "string",
                    context.packageName
                )
            )
            categoriesText += categoryName + if (position > 0) "," else ""
        }
        holder.categories.text = categoriesText
        holder.win.text =
            if (lastMatch.win) context.getText(R.string.win) else context.getText(R.string.loose)


        holder.win.setTextColor(
            if (lastMatch.win) context.resources.getColor(
                R.color.text_win,
                context.resources.newTheme()
            ) else context.resources.getColor(
                R.color.text_loose,
                context.resources.newTheme()
            )
        )

        holder.score.text = lastMatch.score.toString()
        holder.playtime.text = Utils().changePlaytime(context, lastMatch.playtime)

    }
}