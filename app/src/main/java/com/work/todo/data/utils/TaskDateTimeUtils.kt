package com.work.todo.data.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TaskDateTimeUtils {

    private val dbDateFormatter get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val uiDateFormatter get() = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    private val timeFormatter get() = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun convertDateTimeToMillis(dateStr: String, timeStr: String): Long? {
        return try {
            val dateObj = dbDateFormatter.parse(dateStr)
            val timeObj = timeFormatter.parse(timeStr)
            if (dateObj != null && timeObj != null) {
                Calendar.getInstance().run {
                    val dateCal = Calendar.getInstance().apply { time = dateObj }
                    val timeCal = Calendar.getInstance().apply { time = timeObj }
                    set(
                        dateCal.get(Calendar.YEAR),
                        dateCal.get(Calendar.MONTH),
                        dateCal.get(Calendar.DAY_OF_MONTH),
                        timeCal.get(Calendar.HOUR_OF_DAY),
                        timeCal.get(Calendar.MINUTE),
                        0
                    )
                    set(Calendar.MILLISECOND, 0)
                    timeInMillis
                }
            } else null
        } catch (e: Exception) {
            null
        }
    }

    fun formatDbDateToUi(dateStr: String?): String {
        if (dateStr == null) return "Set Date"
        return try {
            val date = dbDateFormatter.parse(dateStr)
            date?.let { uiDateFormatter.format(it) } ?: "Set Date"
        } catch (e: Exception) {
            "Set Date"
        }
    }

    fun getFormattedDatePair(year: Int, month: Int, day: Int): Pair<String, String> {
        val calendar = Calendar.getInstance().apply { set(year, month, day) }
        return Pair(uiDateFormatter.format(calendar.time), dbDateFormatter.format(calendar.time))
    }

    fun getFormattedTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return timeFormatter.format(calendar.time)
    }

    fun getCurrentDbDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun formatComponentsToDbDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance().apply { set(year, month, day) }
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

}