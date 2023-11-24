package com.amel.quran.network.quran

import com.amel.quran.network.Resource
import kotlinx.coroutines.flow.Flow

interface IQuranRepository {
    fun getListSurah() : Flow<Resource<List<Surah>>>

    fun getDetailSurahWithQuranEditions(number: Int) : Flow<Resource<List<QuranEdition>>>
}