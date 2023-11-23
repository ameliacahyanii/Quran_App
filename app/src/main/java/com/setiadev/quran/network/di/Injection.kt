package com.setiadev.quran.network.di

import android.content.Context
import com.setiadev.quran.local.CalendarPreferences
import com.setiadev.quran.local.LocationPreferences
import com.setiadev.quran.network.ApiConfig
import com.setiadev.quran.network.quran.QuranRepository
import com.setiadev.quran.network.RemoteDataSource
import com.setiadev.quran.network.adzan.AdzanRepository

object Injection {
    private val quranApiService = ApiConfig.getQuranService
    private val adzanApiService = ApiConfig.getAdzanTimeService
    private val remoteDataSource = RemoteDataSource(quranApiService, adzanApiService)
    fun provideQuranRepository(): QuranRepository {
        return QuranRepository(remoteDataSource)
    }

    fun provideAdzanRepository(context: Context): AdzanRepository {
        val locationPreferences = LocationPreferences(context)
        val calendarPrefrences = CalendarPreferences()
        return AdzanRepository(remoteDataSource, locationPreferences, calendarPrefrences)
    }
}