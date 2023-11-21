package com.setiadev.quran.network.adzan

import com.setiadev.quran.network.Resource

data class AdzanDataResult(
    val listLocation: List<String>,
    val listCity: Resource<List<City>>
)
