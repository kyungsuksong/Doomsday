package com.kyungsuksong.doomsday.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kyungsuksong.doomsday.util.DATABASE_NAME

@Database(
    entities =
    [
        AsteroidEntity::class,
        PicOfDayEntity::class,
        EarthquakeEntity::class,
        FireEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun asteroidDao(): AsteroidDao
    abstract fun picOfDayDao(): PicOfDayDao
    abstract fun earthquakeDao(): EarthquakeDao
    abstract fun fireDao(): FireDao

    companion object {
        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            val instance = buildDatabase(context)
            INSTANCE = instance
            return instance
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object: RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // start one time worker here if needed
                        }
                    }
                )
                .build()
        }
    }
}