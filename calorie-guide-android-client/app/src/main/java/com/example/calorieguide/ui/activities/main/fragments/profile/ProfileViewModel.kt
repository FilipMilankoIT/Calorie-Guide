package com.example.calorieguide.ui.activities.main.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorieguide.R
import com.example.core.model.ErrorCode
import com.example.core.model.Gender
import com.example.core.model.Profile
import com.example.core.model.RepositoryResult
import com.example.core.model.requests.UpdateProfileRequest
import com.example.core.repository.Repository
import com.example.core.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _profile = MutableLiveData<Profile?>(repository.getProfile())
    val profile: LiveData<Profile?> = _profile

    private val _gender = MutableLiveData<Gender?>(profile.value?.gender)
    val gender: LiveData<Gender?> = _gender

    private val _birthday = MutableLiveData<Long?>(profile.value?.birthday)
    val birthday: LiveData<Long?> = _birthday

    private val _profileUpdated = SingleLiveEvent<Boolean>()
    val profileUpdated: LiveData<Boolean> = _profileUpdated

    private val _userDeleted = SingleLiveEvent<Boolean>()
    val userDeleted: LiveData<Boolean> = _userDeleted

    private val _errorCalorieLimit = MutableLiveData<Int?>(null)
    val errorCalorieLimit: LiveData<Int?> = _errorCalorieLimit

    private val _error = SingleLiveEvent<Int>()
    val error: LiveData<Int> = _error

    private val _tokenError = SingleLiveEvent<Int>()
    val tokenError: LiveData<Int> = _tokenError

    private val _waiting = SingleLiveEvent<Boolean>()
    val waiting: LiveData<Boolean> = _waiting

    init {
        viewModelScope.launch {
            repository.syncProfile()
            _profile.value = repository.getProfile()
        }
    }

    fun setGender(gender: Gender) {
        _gender.value = gender
    }

    fun setBirthday(birthday: Long) {
        _birthday.value = birthday
    }

    fun save(
        firstName: String?,
        lastName: String?,
        dailyCalorieLimit: Int?
    ) {
        val profile = _profile.value ?: return
        if (dailyCalorieLimit == null) {
            _errorCalorieLimit.value = R.string.error_daily_calorie_limit
            return
        }

        val updatedProfile = Profile(profile.username, profile.role, dailyCalorieLimit, firstName,
            lastName, gender.value, birthday.value)
        val request = UpdateProfileRequest(updatedProfile)
        viewModelScope.launch {
            _waiting.value = true
            when(val result = repository.updateProfile(request)) {
                is RepositoryResult.Success -> {
                    _profile.value = updatedProfile
                    _profileUpdated.value = true
                }
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

    fun signOut() {
        _waiting.value = true
        viewModelScope.launch {
            repository.signOut()
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            _waiting.value = true
            when(val result = repository.deleteUser()) {
                is RepositoryResult.Success -> _userDeleted.value = true
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
            _waiting.value = _userDeleted.value == true
        }
    }
}