package com.setiadev.quran.local

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarPreferences {
    private val calendar = Calendar.getInstance().time
    fun getCurrentDate(): List<String> {
        val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar).toString()
        val month = SimpleDateFormat("MM", Locale.getDefault()).format(calendar).toString()
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar).toString()
        val now =
            SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(calendar).toString()
        return listOf(year, month, date, now)
    }
}