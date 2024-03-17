package com.kyungsuksong.doomsday.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kyungsuksong.doomsday.util.asteroid_today
import com.kyungsuksong.doomsday.util.asteroid_tomorrow
import kotlinx.coroutines.flow.Flow

// get doomsday entity from local
@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroids WHERE close_approach_date >= :startDate ORDER BY close_approach_date")
    fun getAsteroidsOnwards(startDate: Long = asteroid_tomorrow): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date BETWEEN :startDate AND :endDate ORDER BY close_approach_date")
    fun getAsteroidOfToday(startDate: Long = asteroid_today, endDate: Long = asteroid_tomorrow - 1L): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date < :endDate ORDER BY close_approach_date")
    fun getAsteroidsOfPast(endDate: Long = asteroid_today): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids WHERE id == :id")
    fun getAsteroidById(id: Long): Flow<AsteroidEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroid(asteroid: AsteroidEntity)
}