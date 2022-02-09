package com.example.calorieguide.ui.utils

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.example.calorieguide.utils.TimeUtils.getStartOfDay
import com.example.calorieguide.utils.TimeUtils.toUTCTime
import java.util.*

object DatePicker {

    fun get(selectedTime: Long?, futureAllowed: Boolean = false): MaterialDatePicker<Long> {
        val calendarConstraintsBuilder = CalendarConstraints.Builder()
            .setEnd(getStartOfDay(Date()).toUTCTime())
        if (!futureAllowed) {
            calendarConstraintsBuilder.setValidator(DateValidatorPointBackward.now())
        }
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
            .setSelection((selectedTime ?: getStartOfDay(Date())).toUTCTime())
            .setCalendarConstraints(calendarConstraintsBuilder.build())

        return datePickerBuilder.build()
    }

}