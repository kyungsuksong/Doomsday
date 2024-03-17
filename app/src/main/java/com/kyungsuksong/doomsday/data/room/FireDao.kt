package com.kyungsuksong.doomsday.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FireDao {
    @Query("SELECT * FROM fires WHERE country = :countryType")
    fun getFiresInCountry(countryType: String): Flow<List<FireEntity>>

    @Query("SELECT * FROM fires WHERE source_type = :sourceType AND country = :countryType")
    fun getFiresInCountryWithSourceType(countryType: String, sourceType: Int): Flow<List<FireEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fireEntity: FireEntity)
}