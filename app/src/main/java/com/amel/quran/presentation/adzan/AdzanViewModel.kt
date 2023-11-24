package com.amel.quran.presentation.adzan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.amel.quran.network.Resource
import com.amel.quran.network.adzan.AdzanDataResult
import com.amel.quran.network.adzan.AdzanRepository

class AdzanViewModel(
    private val adzanRepository: AdzanRepository
) : ViewModel()  {
    fun getDailyAdzanTime():
            LiveData<Resource<AdzanDataResult>> = adzanRepository
                .getResultDailyAdzanTime()
}