package com.example.calorieguide.ui.recyclerview.food

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calorieguide.R

class FoodHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemLayout: View = itemView.findViewById(R.id.item_layout)
    val dateLayout: View = itemView.findViewById(R.id.date_layout)
    val date: TextView = itemView.findViewById(R.id.date)
    val time: TextView = itemView.findViewById(R.id.time)
    val name: TextView = itemView.findViewById(R.id.name)
    val calories: TextView = itemView.findViewById(R.id.calories)
}