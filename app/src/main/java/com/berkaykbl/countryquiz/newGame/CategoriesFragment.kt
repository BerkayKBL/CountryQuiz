package com.berkaykbl.countryquiz.newGame

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkaykbl.countryquiz.R
import com.berkaykbl.countryquiz.adapter.CategoriesAdapter
import com.berkaykbl.countryquiz.databinding.FragmentCategoriesBinding

private var selectedCategories: ArrayList<String> = ArrayList<String>()

class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private var categoriesList: List<String> = ArrayList()
    private var categoryAdapter: CategoriesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedCategories.clear()
        val categoriesArray = ArrayList<HashMap<String, String>>()
        categoriesList = resources.getStringArray(R.array.categories).toList()
        requireView().findViewById<RecyclerView>(R.id.categories).layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        categoriesList.forEach {
            val categoryHash = HashMap<String, String>()
            val categoryName = resources.getString(
                resources.getIdentifier(
                    "category.$it", "string", requireContext().packageName
                )
            )
            categoryHash.put(it, categoryName)
            categoriesArray.add(categoryHash)
        }


        categoryAdapter = CategoriesAdapter(
            categoriesArray, selectedCategories
        ) { b: Boolean, s: String ->
            categorySelectCallbak(b, s)
        }
        view.findViewById<RecyclerView>(R.id.categories).adapter = categoryAdapter


    }


    @SuppressLint("NotifyDataSetChanged")
    fun categorySelectCallbak(isSelected: Boolean, key: String) {
        if (key == "all" && isSelected) {
            selectedCategories.addAll(categoriesList)
            categoryAdapter!!.notifyDataSetChanged()
        } else if (key == "all" && !isSelected) {
            selectedCategories.clear()
            categoryAdapter!!.notifyDataSetChanged()
        } else if (isSelected) {
            selectedCategories.add(key)
            if (selectedCategories.size == categoriesList.size - 1) {
                selectedCategories.add("all")
                categoryAdapter!!.notifyDataSetChanged()

            }
        } else {
            selectedCategories.remove(key)
            if (selectedCategories.contains("all")) {
                selectedCategories.remove("all")
                categoryAdapter!!.notifyDataSetChanged()
            }
        }
    }

    fun getSelectedCategories(): ArrayList<String> {
        return selectedCategories
    }
}