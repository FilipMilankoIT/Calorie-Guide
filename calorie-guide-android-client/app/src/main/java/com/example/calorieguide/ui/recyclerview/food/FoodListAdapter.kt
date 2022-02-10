package com.example.calorieguide.ui.recyclerview.food

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.calorieguide.R
import com.example.calorieguide.utils.TimeUtils.toFormattedTime
import com.example.core.model.Food

class FoodListAdapter(
    private val context: Context,
    private val onClickListener: (item: Food) -> Unit
): PagedListAdapter<Food, FoodHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Food>() {

            override fun areItemsTheSame(old: Food, new: Food) = old.id == new.id

            override fun areContentsTheSame(old: Food, new: Food) = old == new
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_item, parent, false)
        return FoodHolder(view)
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        getItem(holder.adapterPosition)?.let { item ->
            holder.time.text = item.timestamp.toFormattedTime()
            holder.name.text = item.name
            holder.calories.text = context.getString(R.string.calories_count, item.calories)
            holder.root.setOnClickListener { onClickListener(item) }
        }
    }
}