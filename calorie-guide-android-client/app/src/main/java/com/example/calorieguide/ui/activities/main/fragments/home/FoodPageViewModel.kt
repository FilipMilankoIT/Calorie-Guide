package com.example.calorieguide.ui.activities.main.fragments.home

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.example.calorieguide.utils.TimeUtils.DAY
import com.example.calorieguide.utils.TimeUtils.SECOND
import com.example.calorieguide.utils.TimeUtils.getStartOfDay
import com.example.core.model.Food
import com.example.core.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FoodPageViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private var _startTime = getStartOfDay(Date())

    private var _endTime = _startTime + DAY - SECOND

    private var _foodList = repository.getMyFoodEntries(_startTime, _endTime)

    private val _calorieSum = MutableLiveData(0)
    val calorieSum: LiveData<Int> = _calorieSum

    fun initList(startTime: Long, endTime: Long, lifecycleOwner: LifecycleOwner,
                 observer: Observer<PagedList<Food>>) {
        _startTime = startTime
        _endTime = endTime
        _foodList.removeObserver(observer)
        _foodList = repository.getMyFoodEntries(startTime, endTime)
        _foodList.observe(lifecycleOwner, observer)
    }

    fun updateList(lifecycleOwner: LifecycleOwner, observer: Observer<PagedList<Food>>) {
        _foodList.removeObserver(observer)
        _foodList = repository.getMyFoodEntries(_startTime, _endTime)
        _foodList.observe(lifecycleOwner, observer)
    }

    fun getStartTime() = _startTime

    fun getEndTime() = _endTime

    fun updateCalorieSum() {
        viewModelScope.launch {
            _calorieSum.value = repository.getMyCalorieSumByTimeRange(_startTime, _endTime)
        }
    }
}