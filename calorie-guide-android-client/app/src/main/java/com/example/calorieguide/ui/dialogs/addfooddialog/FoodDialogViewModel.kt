package com.example.calorieguide.ui.dialogs.addfooddialog

import androidx.lifecycle.*
import com.example.calorieguide.R
import com.example.core.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDialogViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    var selectedDate: Long? = null

    private val _savedFoodNames = MutableLiveData<List<String>>()
    val savedFoodNames: LiveData<List<String>> = _savedFoodNames

    private val _timeDate = MutableLiveData<Long?>()
    val timeDate: LiveData<Long?> = _timeDate

    private val _nameError = MutableLiveData<Int?>(null)
    val nameError: LiveData<Int?> = _nameError

    private val _timeDateError = MutableLiveData<Int?>(null)
    val timeDateError: LiveData<Int?> = _timeDateError

    private val _caloriesError = MutableLiveData<Int?>(null)
    val caloriesError: LiveData<Int?> = _caloriesError

    init {
        viewModelScope.launch {
            _savedFoodNames.value = repository.getAllSavedFoodNames()
        }
    }

    fun setTimeDate(time: Long) {
        _timeDate.value = time
    }

    fun validateInput(name: String, calories: Int?): Boolean {
        var valid = true

        if (name.isBlank()) {
            _nameError.value = R.string.empty_food_name
            valid = false
        } else {
            _nameError.value = null
        }

        if (timeDate.value == null) {
            _timeDateError.value = R.string.empty_time_date
            valid = false
        } else {
            _timeDateError.value = null
        }

        if (calories == null) {
            _caloriesError.value = R.string.empty_calories
            valid = false
        } else {
            _caloriesError.value = null
        }

        return valid
    }
}