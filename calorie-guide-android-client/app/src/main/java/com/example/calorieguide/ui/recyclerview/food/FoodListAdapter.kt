package com.example.calorieguide.ui.recyclerview.food

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.calorieguide.R
import com.example.calorieguide.utils.TimeUtils.toFormattedDate
import com.example.calorieguide.utils.TimeUtils.toFormattedTime
import com.example.core.model.Food

class FoodListAdapter(
    private val context: Context,
    private val showDate: Boolean,
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
        getItem(position)?.let { item ->
            val date = item.timestamp.toFormattedDate()
            holder.dateLayout.visibility = if (
                showDate && (position == 0
                        || date != getItem(position - 1)?.timestamp?.toFormattedDate())
            ) {
                holder.date.text = date
                View.VISIBLE
            } else View.GONE

            holder.time.text = item.timestamp.toFormattedTime()
            holder.name.text = item.name
            holder.calories.text = context.getString(R.string.calories_count, item.calories)
            holder.itemLayout.setOnClickListener { onClickListener(item) }
        }
    }

    override fun onCurrentListChanged(
        previousList: PagedList<Food>?,
        currentList: PagedList<Food>?
    ) {
        super.onCurrentListChanged(previousList, currentList)
        if (showDate) {
            currentList?.forEachIndexed { index, item ->
                if (index == 0 || item.timestamp.toFormattedDate() !=
                    currentList[index - 1]?.timestamp?.toFormattedDate()) {
                    notifyItemChanged(index)
                }
            }
            previousList?.forEachIndexed { index, item ->
                if (index == 0 || item.timestamp.toFormattedDate() !=
                    previousList[index - 1]?.timestamp?.toFormattedDate()) {
                        currentList?.find { it.id == item.id }?.let {
                            notifyItemChanged(currentList.indexOf(it))
                        }
                }
            }
        }
    }
}