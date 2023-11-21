package com.setiadev.quran.presentation.adzan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.setiadev.quran.network.Resource
import com.setiadev.quran.network.adzan.AdzanDataResult
import com.setiadev.quran.network.adzan.AdzanRepository

class AdzanViewModel(
    private val adzanRepository: AdzanRepository
) : ViewModel()  {
    fun getDailyAdzanTime():
            LiveData<Resource<AdzanDataResult>> = adzanRepository
                .getResultDailyAdzanTime()
}