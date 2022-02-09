package com.example.calorieguide.ui.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.calorieguide.ui.activities.main.fragments.home.FoodPageFragment
import android.os.Bundle
import com.example.calorieguide.utils.TimeUtils.DAY
import com.example.calorieguide.utils.TimeUtils.SECOND
import com.example.calorieguide.utils.TimeUtils.getStartOfDay
import java.util.*


class FoodPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    companion object {
        const val LEFT_END_POSITION = 0
        const val START_POSITION: Int = Int.MAX_VALUE / 2
        const val RIGHT_END_POSITION = Int.MAX_VALUE

        fun getStartTimeFromPosition(position: Int) =
            getStartOfDay(Date()) + (position - START_POSITION) * DAY
    }

    override fun getItemCount(): Int = RIGHT_END_POSITION

    override fun createFragment(position: Int): Fragment {
        val fragment = FoodPageFragment()
        val args = Bundle()
        val startTime = getStartTimeFromPosition(position)
        args.putLong(FoodPageFragment.START_TIME_KEY, startTime)
        args.putLong(FoodPageFragment.END_TIME_KEY, startTime + DAY - SECOND)
        fragment.arguments = args
        return fragment
    }
}