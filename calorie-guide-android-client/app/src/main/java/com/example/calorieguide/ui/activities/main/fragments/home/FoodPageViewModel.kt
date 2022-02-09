package com.example.calorieguide.ui.activities.main.fragments.home

import androidx.lifecycle.*
import com.example.calorieguide.utils.TimeUtils.DAY
import com.example.calorieguide.utils.TimeUtils.SECOND
import com.example.calorieguide.utils.TimeUtils.getStartOfDay
import com.example.core.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FoodPageViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private var _startTime = getStartOfDay(Date())

    private var _endTime = _startTime + DAY - SECOND

    private var _foodList = repository.getMyFoodEntries(_startTime, _endTime)

    fun getFoodList() = _foodList

    fun initList(startTime: Long, endTime: Long) {
        _startTime = startTime
        _endTime = endTime
        _foodList = repository.getMyFoodEntries(startTime, endTime)
    }

    fun updateList(owner: LifecycleOwner) {
        _foodList.removeObservers(owner)
        _foodList = repository.getMyFoodEntries(_startTime, _endTime)
    }

    fun getStartTime() = _startTime

    fun getEndTime() = _endTime
}