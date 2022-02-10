package com.example.calorieguide.ui.utils

import androidx.fragment.app.FragmentManager
import java.util.*

object DateTimePicker {

    fun show(
        fragmentManager: FragmentManager,
        tag: String,
        selectedTime: Long?,
        futureAllowed: Boolean = true,
        callback: (time: Long) -> Unit,
    ) {
        val datePicker = DatePicker.get(selectedTime, futureAllowed)
        datePicker.addOnPositiveButtonClickListener { dateTime ->
            if (dateTime != null) {
                val timePicker = TimePicker.get(selectedTime)
                timePicker.addOnPositiveButtonClickListener {
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = dateTime
                    cal[Calendar.HOUR_OF_DAY] = timePicker.hour
                    cal[Calendar.MINUTE] = timePicker.minute
                    callback(cal.timeInMillis)
                }
                timePicker.show(fragmentManager, tag)
            }
        }
        datePicker.show(fragmentManager, tag)
    }
}