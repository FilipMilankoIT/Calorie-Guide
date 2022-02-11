package com.example.calorieguide.ui.activities.main.fragments.users

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.calorieguide.R
import com.example.core.model.ErrorCode
import com.example.core.model.RepositoryResult
import com.example.core.model.User
import com.example.core.repository.Repository
import com.example.core.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private val _userList = MutableLiveData<PagedList<User>>()
    val userList: LiveData<PagedList<User>> = _userList

    private val rootUserListObserver = Observer<PagedList<User>> {
        _userList.value = it
    }

    private var rootUserList = repository.getUserEntries().apply {
        observeForever(rootUserListObserver)
    }

    private val _tokenError = SingleLiveEvent<Int>()
    val tokenError: LiveData<Int> = _tokenError

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            when(val result = repository.syncUserEntries(null)) {
                is RepositoryResult.Error -> {
                    if (result.code == ErrorCode.UNAUTHORIZED.code) {
                        _tokenError.value = R.string.token_expired
                    }
                }
            }
            _isRefreshing.value = false
        }
    }

    fun updateUserList() {
        rootUserList.removeObserver(rootUserListObserver)
        rootUserList = repository.getUserEntries()
        rootUserList.observeForever(rootUserListObserver)
    }

    override fun onCleared() {
        super.onCleared()
        rootUserList.removeObserver(rootUserListObserver)
    }
}