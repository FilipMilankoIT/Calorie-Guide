package com.example.calorieguide.ui.recyclerview.useraveragecalories

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.calorieguide.R
import com.example.core.model.UserAverageCalories

class UserAverageCaloriesListAdapter(
    private val context: Context
): PagedListAdapter<UserAverageCalories, UserAverageCaloriesHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserAverageCalories>() {

            override fun areItemsTheSame(old: UserAverageCalories, new: UserAverageCalories) =
                old.username == new.username

            override fun areContentsTheSame(old: UserAverageCalories, new: UserAverageCalories) = old == new
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAverageCaloriesHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_avrage_calories_item, parent, false)
        return UserAverageCaloriesHolder(view)
    }

    override fun onBindViewHolder(holder: UserAverageCaloriesHolder, position: Int) {
        getItem(holder.adapterPosition)?.let { item ->
            holder.username.text = item.username
            holder.calories.text = context.getString(R.string.calories_count, item.count)
        }
    }
}