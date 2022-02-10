package com.example.calorieguide.ui.utils

import com.example.calorieguide.utils.TimeUtils.getHour
import com.example.calorieguide.utils.TimeUtils.getMinutes
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

object TimePicker {

    fun get(selectedTime: Long?) = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setHour(selectedTime?.getHour() ?: 0)
        .setMinute(selectedTime?.getMinutes() ?: 0)
        .build()
}