package com.example.calorieguide.ui.recyclerview.user

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calorieguide.R

class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val root: View = itemView.findViewById(R.id.root)
    val username: TextView = itemView.findViewById(R.id.username)
}