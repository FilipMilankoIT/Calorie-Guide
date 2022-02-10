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

    fun Long.toFormattedTime(): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(Date(this))
    }

    fun Long.toFormattedTimeDate(): String {
        val formatter = SimpleDateFormat("HH:mm dd.MM.yyyy.", Locale.getDefault())
        return formatter.format(Date(this))
    }

    fun getStartOfDay(time: Date): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time.time / 1000 * 1000
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        return cal.timeInMillis
    }

    fun Long.getHour(): Int {
        val time = Date(this)
        val cal = Calendar.getInstance()
        cal.timeInMillis = time.time
        return cal[Calendar.HOUR_OF_DAY]
    }

    fun Long.getMinutes(): Int {
        val time = Date(this)
        val cal = Calendar.getInstance()
        cal.timeInMillis = time.time
        return cal[Calendar.MINUTE]
    }

    fun Long.toUTCTime(): Long {
        val tz = TimeZone.getDefault()
        return this + tz.getOffset(Date().time)
    }

    fun Long.toLocalTime(): Long {
        val tz = TimeZone.getDefault()
        return this - tz.getOffset(Date().time)
    }
}