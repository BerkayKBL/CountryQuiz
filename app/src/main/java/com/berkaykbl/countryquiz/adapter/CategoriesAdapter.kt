package com.berkaykbl.countryquiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.berkaykbl.countryquiz.R

class CategoriesAdapter(
    private val categoriesList: ArrayList<HashMap<String, String>>,
    private val selectedCategories: ArrayList<String>,
    private val callback: (Boolean, String) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.categoryName)
        val checkbox: ImageView = view.findViewById(R.id.checkBox)
        val categoryLayout: LinearLayout = view.findViewById(R.id.categoryLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.element_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoriesList[position]
        val key = category.keys.toList()[0]
        val name = category.values.toList()[0]
        holder.checkbox.isSelected = selectedCategories.contains(key)
        holder.categoryName.text = name
        holder.categoryLayout.setOnClickListener {
            val isSelected = !holder.checkbox.isSelected
            holder.checkbox.isSelected = isSelected
            callback(isSelected, key)
        }
    }

}