package com.setiadev.quran.network.adzan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.setiadev.quran.local.LocationPreferences
import com.setiadev.quran.network.NetworkBoundResource
import com.setiadev.quran.network.RemoteDataSource
import com.setiadev.quran.network.Resource
import com.setiadev.quran.network.NetworkResponse
import com.setiadev.quran.utils.DataMapper
import kotlinx.coroutines.flow.Flow

class AdzanRepository(
    private val remoteDataSource: RemoteDataSource,
    private val locationPreferences: LocationPreferences,
) : IAdzanRepository {
    override fun getLastKnownLocation(): LiveData<List<String>> =
        locationPreferences.getLastKnownLocation()

    override fun searchCity(city: String): Flow<Resource<List<City>>> {
        return object : NetworkBoundResource<List<City>, List<CityItem>>() {
            override fun fetchFromNetwork(data: List<CityItem>): Flow<List<City>> {
                return DataMapper.mapResponseToDomain(data)
            }

            override suspend fun createCall(): Flow<NetworkResponse<List<CityItem>>> {
                return remoteDataSource.searchCity(city)
            }
        }.asFlow()
    }

    fun getResultDailyAdzanTime(): LiveData<Resource<AdzanDataResult>> {

        val liveDataLocation = getLastKnownLocation()
        val liveDataCityId = getLastKnownLocation().switchMap { listLocation ->
            searchCity(listLocation[0]).asLiveData()
        }

        val result = MediatorLiveData<Resource<AdzanDataResult>>()

        result.addSource(liveDataLocation) {
            result.value = combineLatestData(liveDataLocation, liveDataCityId)
        }
        result.addSource(liveDataCityId) {
            result.value = combineLatestData(liveDataLocation, liveDataCityId)
        }
        return result
    }

    private fun combineLatestData(
        listLocationResult: LiveData<List<String>>,
        listCityResult: LiveData<Resource<List<City>>>
    ): Resource<AdzanDataResult> {

        val listLocation = listLocationResult.value
        val listCity = listCityResult.value

        // Don't send a success until we have both results
        return if (listLocation == null || listCity == null) {
            Resource.Loading()
        } else {
            try {
                Resource.Success(
                    AdzanDataResult(
                        listLocation = listLocation,
                        listCity = listCity
                    )
                )
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage)
            }
        }
    }
}