package com.example.calorieguide.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    const val SECOND = 1000
    const val MINUTE = 60 * SECOND
    const val HOUR = 60 * MINUTE
    const val DAY = 24 * HOUR

    fun Long.toFormattedDate(): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault())
        return formatter.format(Date(this))
    }

    fun Long.toFormattedUTCDate(): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date(this))
    }

    fun Long.toFormattedTime(): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(Date(this))
    }

    fun getStartOfDay(time: Date): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time.time
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        return cal.timeInMillis
    }
}