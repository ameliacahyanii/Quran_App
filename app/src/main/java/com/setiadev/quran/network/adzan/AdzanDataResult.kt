package com.setiadev.quran.network.adzan

import com.setiadev.quran.network.Resource

data class AdzanDataResult(
    val listLocation: List<String>,
    val dailyAdzan: Resource<DailyAdzan>,
    val listCalendar: List<String>
)
