package com.kyungsuksong.doomsday.repo

import com.kyungsuksong.doomsday.data.api.AsteroidApiService
import com.kyungsuksong.doomsday.data.api.model.Asteroid
import com.kyungsuksong.doomsday.data.api.model.PicOfDay
import com.kyungsuksong.doomsday.data.room.AsteroidDao
import com.kyungsuksong.doomsday.data.room.PicOfDayDao
import com.kyungsuksong.doomsday.data.room.PicOfDayEntity
import com.kyungsuksong.doomsday.util.IMAGE_MEDIA
import com.kyungsuksong.doomsday.util.convertToAsteroidEntity
import com.kyungsuksong.doomsday.util.parseAsteroidJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import javax.inject.Singleton

@Singleton
class AsteroidRepository(
    private val apiService: AsteroidApiService,
    private val asteroidDao: AsteroidDao,
    private val picOfDayDao: PicOfDayDao
) {
    companion object {
        @Volatile
        var INSTANCE: AsteroidRepository? = null

        fun getInstance(
            apiService: AsteroidApiService,
            asteroidDao: AsteroidDao,
            picOfDayDao: PicOfDayDao
        ) = INSTANCE ?: synchronized(this) {
                val instance = AsteroidRepository(apiService, asteroidDao, picOfDayDao)
                INSTANCE = instance
                return instance
            }
    }

    /**
     * Methods used to fetch asteroids from Room
     */
    fun getPicOfDayFromLocal() = picOfDayDao.getPicOfDayFromLocal()
    fun getListOfOnwardsAsteroidFromLocal() = asteroidDao.getAsteroidsOnwards()
    fun getListOfTodayAsteroidFromLocal() = asteroidDao.getAsteroidOfToday()
    fun getListOfPastAsteroidFromLocal() = asteroidDao.getAsteroidsOfPast()
    fun getAsteroidById(id: Long) = asteroidDao.getAsteroidById(id)

    /**
     * Methods used to fetch asteroids from api
     */
    suspend fun fetchAndSavePicture() {
        /**
         * Fetches the picture of the day data and checks if the media type is image.
         * If true, saves the image
         * */
        withContext(Dispatchers.IO) {
            val response: PicOfDay = apiService.getPicOfDay()
            if (response.mediaType == IMAGE_MEDIA) {
                val pictureEntity = PicOfDayEntity(
                    response.mediaType,
                    response.title,
                    response.url
                )
                picOfDayDao.insertPicture(pictureEntity)
            }
        }
    }

    suspend fun fetchAndSaveAsteroids() {
        /**
         * Fetches the 7 days worth of asteroid data once a day and saves in Room
         */
        withContext(Dispatchers.IO) {
            val response: ResponseBody = apiService.getAsteroids()
            // use Moshi to convert JSON into Asteroid object
            val asteroidList = parseAsteroidJsonResult(
                JSONObject(response.string())
            ) as List<Asteroid>

            convertToAsteroidEntity(asteroidList).forEach {
                asteroidDao.insertAsteroid(it)
            }
        }
    }
}