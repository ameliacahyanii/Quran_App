package com.setiadev.quran.presentation.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.setiadev.quran.network.quran.QuranRepository

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