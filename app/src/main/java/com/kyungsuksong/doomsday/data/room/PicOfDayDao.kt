package com.kyungsuksong.doomsday.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PicOfDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: PicOfDayEntity)

    @Query("SELECT * FROM pictures")
    fun getPicOfDayFromLocal(): Flow<PicOfDayEntity?>
}