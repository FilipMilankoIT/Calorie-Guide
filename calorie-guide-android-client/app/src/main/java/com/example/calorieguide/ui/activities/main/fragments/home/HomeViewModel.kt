package com.example.calorieguide.ui.activities.main.fragments.home

import androidx.lifecycle.*
import com.example.calorieguide.R
import com.example.calorieguide.ui.pager.FoodPagerAdapter
import com.example.core.model.ErrorCode
import com.example.core.model.Food
import com.example.core.model.RepositoryResult
import com.example.core.model.requests.AddFoodRequest
import com.example.core.repository.Repository
import com.example.core.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    var currentPage = FoodPagerAdapter.START_POSITION

    private val _isFilterOn = MutableLiveData<Boolean>()
    val isFilterOn: LiveData<Boolean> = _isFilterOn

    private val _error = SingleLiveEvent<Int>()
    val error: LiveData<Int> = _error

    private val _tokenError = SingleLiveEvent<Int>()
    val tokenError: LiveData<Int> = _tokenError

    private val _waiting = MutableLiveData<Boolean>()
    val waiting: LiveData<Boolean> = _waiting

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    fun refreshData(from: Long, to: Long) {
        viewModelScope.launch {
            _isRefreshing.value = true
            when(val result = repository.syncMyFoodEntries(from, to)) {
                is RepositoryResult.Error -> {
                    if (result.code == ErrorCode.UNAUTHORIZED.code) {
                        _tokenError.value = R.string.token_expired
                    }
                }
            }
            _isRefreshing.value = false
        }
    }

    fun toggleFilter() {
        _isFilterOn.value = _isFilterOn.value != true
    }

    fun addFoodItem(food: Food) {
        viewModelScope.launch {
            _waiting.value = true
            val request = AddFoodRequest(null, food.name, food.timestamp, food.calories)
            when(val result = repository.addFood(request)) {
                is RepositoryResult.Success -> _isRefreshing.value = false
                is RepositoryResult.Error -> {
                    if (result.code == ErrorCode.UNAUTHORIZED.code) {
                        _tokenError.value = R.string.token_expired
                    } else {
                        _error.value = R.string.error_unknown
                    }
                }
                is RepositoryResult.NetworkError -> _error.value = R.string.error_no_network
                is RepositoryResult.UnknownError -> _error.value = R.string.error_unknown
            }
            _waiting.value = false
        }
    }
}