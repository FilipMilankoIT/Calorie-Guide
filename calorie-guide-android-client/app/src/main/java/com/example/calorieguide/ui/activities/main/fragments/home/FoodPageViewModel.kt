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

    private var startTime = getStartOfDay(Date())
    private var endTime = startTime + DAY - SECOND
    private var username: String? = null

    private val _foodList = MutableLiveData<PagedList<Food>>()
    val foodList: LiveData<PagedList<Food>> = _foodList

    private val rootFoodListObserver = Observer<PagedList<Food>> {
        _foodList.value = it
    }

    private var rootFoodList: LiveData<PagedList<Food>> = MutableLiveData()

    private val _calorieSum = MutableLiveData(0)
    val calorieSum: LiveData<Int> = _calorieSum

    fun initList(startTime: Long, endTime: Long, username: String?) {
        this.startTime = startTime
        this.endTime = endTime
        this.username = username
        updateList()
    }

    fun updateList() {
        rootFoodList.removeObserver(rootFoodListObserver)
        val username = username
        rootFoodList = if (username == null) repository.getMyFoodEntries(startTime, endTime)
        else repository.getFoodEntries(username, startTime, endTime)
        rootFoodList.observeForever(rootFoodListObserver)
    }

    fun getStartTime() = startTime

    fun getEndTime() = endTime

    fun updateCalorieSum() {
        viewModelScope.launch {
            val username = username
            _calorieSum.value = if (username == null)
                repository.getMyCalorieSumByTimeRange(startTime, endTime)
            else repository.getCalorieSumByTimeRange(username, startTime, endTime)
        }
    }

    override fun onCleared() {
        super.onCleared()
        rootFoodList.removeObserver(rootFoodListObserver)
    }
}