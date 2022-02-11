package com.example.calorieguide.ui.activities.main.fragments.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.calorieguide.R
import com.example.calorieguide.utils.TimeUtils
import com.example.calorieguide.utils.TimeUtils.DAY
import com.example.calorieguide.utils.TimeUtils.SECOND
import com.example.calorieguide.utils.TimeUtils.WEEK
import com.example.core.model.ErrorCode
import com.example.core.model.RepositoryResult
import com.example.core.model.UserAverageCalories
import com.example.core.repository.Repository
import com.example.core.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private val _userList = MutableLiveData<PagedList<UserAverageCalories>>()
    val userList: LiveData<PagedList<UserAverageCalories>> = _userList

    private val _thisWeekCount = MutableLiveData(0)
    val thisWeekCount: LiveData<Int> = _thisWeekCount

    private val _lastWeekCount = MutableLiveData(0)
    val lastWeekCount: LiveData<Int> = _lastWeekCount

    private val _error = SingleLiveEvent<Int>()
    val error: LiveData<Int> = _error

    private val _tokenError = SingleLiveEvent<Int>()
    val tokenError: LiveData<Int> = _tokenError

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    fun refreshReport() {
        viewModelScope.launch {
            _isRefreshing.value = true

            val startOfThisWeek = TimeUtils.getStartOfDay(Date()) - WEEK + DAY
            val endOfThisWeek = startOfThisWeek + WEEK - SECOND
            val startOfLastWeek = startOfThisWeek - WEEK
            val endOfLastWeek = startOfThisWeek - SECOND

            handleResult(repository.getFoodEntryCount(startOfThisWeek, endOfThisWeek))?.let {
                _thisWeekCount.value = it
            }

            handleResult(repository.getFoodEntryCount(startOfLastWeek, endOfLastWeek))?.let {
                _lastWeekCount.value = it
            }

            handleResult(repository.getUsersAverageCalories(startOfThisWeek, endOfThisWeek))?.let {
                _userList.value = it
            }

            _isRefreshing.value = false
        }
    }

    private fun <T> handleResult(result: RepositoryResult<T>): T? =
        when(result) {
            is RepositoryResult.Success -> result.data
            is RepositoryResult.Error -> {
                if (result.code == ErrorCode.UNAUTHORIZED.code) {
                    _tokenError.value = R.string.token_expired
                } else {
                    _error.value = R.string.error_unknown
                }
                null
            }
            is RepositoryResult.NetworkError -> {
                _error.value = R.string.error_no_network
                null
            }
            is RepositoryResult.UnknownError -> {
                _error.value = R.string.error_unknown
                null
            }
        }
}