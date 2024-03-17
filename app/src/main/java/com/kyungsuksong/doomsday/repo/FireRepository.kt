package com.kyungsuksong.doomsday.repo

import com.kyungsuksong.doomsday.data.api.FireApiService
import com.kyungsuksong.doomsday.data.api.model.Fire
import com.kyungsuksong.doomsday.data.room.FireDao
import com.kyungsuksong.doomsday.util.convertToFireEntity
import com.kyungsuksong.doomsday.util.parseFireResult
import com.kyungsuksong.doomsday.viewmodel.FireSourceType.VIIRS_NOAA20_NRT
import com.kyungsuksong.doomsday.viewmodel.FireSourceType.VIIRS_NOAA21_NRT
import com.kyungsuksong.doomsday.viewmodel.FireSourceType.VIIRS_SNPP_NRT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Singleton

@Singleton
class FireRepository(
    private val apiService: FireApiService,
    private val fireDao: FireDao
) {
    companion object {
        @Volatile
        var INSTANCE: FireRepository? = null

        fun getInstance(
            apiService: FireApiService,
            fireDao: FireDao
        ) = INSTANCE ?: synchronized(this) {
            val instance = FireRepository(apiService, fireDao)
            INSTANCE = instance
            return instance
        }
    }

    /**
     * Methods used to fetch fires from Room
     */
    fun getFiresInCountryWithSourceTypeFromLocal(countryType: String, sourceType: Int) =
        fireDao.getFiresInCountryWithSourceType(countryType, sourceType)

    fun getFiresInCountryFromLocal(countryType: String) =
        fireDao.getFiresInCountry(countryType)

    /**
     * Methods used to fetch earthquakes from api
     */
    suspend fun fetchAndSaveFiresFromViirSnppNrtSource(country: String) {
        /**
         * Fetches the fire data from VIIR SNPP NRT source
         */
        withContext(Dispatchers.IO) {
            val response: ResponseBody = apiService.getFiresFromViirSnppNrtSource(country)

            val fireList = parseFireResult(response.string(), VIIRS_SNPP_NRT) as List<Fire>

            convertToFireEntity(fireList).forEach {
                fireDao.insert(it)
            }
        }
    }

    suspend fun fetchAndSaveFiresFromViirNoaa20NrtSource(country: String) {
        /**
         * Fetches the fire data from VIIR NOAA20 NRT source
         */
        withContext(Dispatchers.IO) {
            val response: ResponseBody = apiService.getFiresFromViirNoaa20NrtSource(country)

            val fireList = parseFireResult(response.string(), VIIRS_NOAA20_NRT) as List<Fire>

            convertToFireEntity(fireList).forEach {
                fireDao.insert(it)
            }
        }
    }

    suspend fun fetchAndSaveFiresFromViirNoaa21NrtSource(country: String) {
        /**
         * Fetches the fire data from VIIR NOAA21 NRT source
         */
        withContext(Dispatchers.IO) {
            val response: ResponseBody = apiService.getFiresFromViirNoaa21NrtSource(country)

            val fireList = parseFireResult(response.string(), VIIRS_NOAA21_NRT) as List<Fire>

            convertToFireEntity(fireList).forEach {
                fireDao.insert(it)
            }
        }
    }
}