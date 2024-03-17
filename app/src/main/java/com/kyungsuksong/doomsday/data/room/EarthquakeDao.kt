package com.kyungsuksong.doomsday.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EarthquakeDao {
    @Query("SELECT * FROM earthquakes WHERE (time BETWEEN :startTime AND :endTime) AND " +
            "(mag BETWEEN :startMag AND :endMag) ORDER BY mag DESC")
    fun getListOfEarthquakes(
        startMag: Double, endMag: Double, startTime: Long, endTime: Long
    ): Flow<List<EarthquakeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEarthquake(earthquakeEntity: EarthquakeEntity)
}