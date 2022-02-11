package com.example.calorieguide.ui.recyclerview.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.calorieguide.R
import com.example.core.model.User

class UserListAdapter(
    private val onClickListener: (item: User) -> Unit
): PagedListAdapter<User, UserHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(old: User, new: User) = old.username == new.username

            override fun areContentsTheSame(old: User, new: User) = old == new
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return UserHolder(view)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        getItem(holder.adapterPosition)?.let { item ->
            holder.username.text = item.username
            holder.root.setOnClickListener { onClickListener(item) }
        }
    }
}