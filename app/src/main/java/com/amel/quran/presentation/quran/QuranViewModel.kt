package com.amel.quran.presentation.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amel.quran.network.quran.QuranRepository

class QuranViewModel(private val quranRepository: QuranRepository) : ViewModel() {

    fun getListSurah() =
        quranRepository
            .getListSurah()
            .asLiveData()

    fun getDetailSurahWithQuranEdition(number: Int) =
        quranRepository
            .getDetailSurahWithQuranEditions(number)
            .asLiveData()
}