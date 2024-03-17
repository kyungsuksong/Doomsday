package com.kyungsuksong.doomsday.di

import android.content.Context
import androidx.work.WorkManager
import com.kyungsuksong.doomsday.data.api.AsteroidApiService
import com.kyungsuksong.doomsday.data.api.EarthquakeApiService
import com.kyungsuksong.doomsday.data.api.FireApiService
import com.kyungsuksong.doomsday.data.room.AppDatabase
import com.kyungsuksong.doomsday.data.room.AsteroidDao
import com.kyungsuksong.doomsday.data.room.EarthquakeDao
import com.kyungsuksong.doomsday.data.room.FireDao
import com.kyungsuksong.doomsday.data.room.PicOfDayDao
import com.kyungsuksong.doomsday.repo.AsteroidRepository
import com.kyungsuksong.doomsday.repo.EarthquakeRepository
import com.kyungsuksong.doomsday.repo.FireRepository
import com.kyungsuksong.doomsday.util.BASE_URL_ASTEROID
import com.kyungsuksong.doomsday.util.BASE_URL_EARTHQUAKE
import com.kyungsuksong.doomsday.util.BASE_URL_FIRE
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideAsteroidDao(appDatabase: AppDatabase): AsteroidDao {
        return appDatabase.asteroidDao()
    }

    @Provides
    fun providePicOfDayDao(appDatabase: AppDatabase): PicOfDayDao {
        return appDatabase.picOfDayDao()
    }

    @Provides
    fun provideEarthquakeDao(appDatabase: AppDatabase): EarthquakeDao {
        return appDatabase.earthquakeDao()
    }

    @Provides
    fun provideFireDAo(appDatabase: AppDatabase): FireDao {
        return appDatabase.fireDao()
    }

    @Singleton
    @Provides
    fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun getOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun getMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun providesWorkManager(@ApplicationContext app: Context) =
        WorkManager.getInstance(app)

    /*
     * Asteroid
     */
    @Singleton
    @Provides
    @Named("asteroid")
    fun getAsteroidRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_ASTEROID)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun getAsteroidApiService(@Named("asteroid") retrofit: Retrofit): AsteroidApiService {
        return retrofit.create(AsteroidApiService::class.java)
    }

    @Singleton
    @Provides
    fun getAsteroidRepository(
        apiService: AsteroidApiService,
        asteroidDao: AsteroidDao,
        picOfDayDao: PicOfDayDao
    ): AsteroidRepository {
        return AsteroidRepository.getInstance(apiService, asteroidDao, picOfDayDao)
    }

    /*
     * Earthquake
     */
    @Singleton
    @Provides
    @Named("earthquake")
    fun getEarthquakeRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_EARTHQUAKE)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun getEarthquakeApiService(@Named("earthquake") retrofit: Retrofit): EarthquakeApiService {
        return retrofit.create(EarthquakeApiService::class.java)
    }

    @Singleton
    @Provides
    fun getEarthquakeRepository(
        apiService: EarthquakeApiService,
        earthquakeDao: EarthquakeDao
    ): EarthquakeRepository {
        return EarthquakeRepository.getInstance(apiService, earthquakeDao)
    }

    /*
     * Fire
     */
    @Singleton
    @Provides
    @Named("fire")
    fun getFireRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_FIRE)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun getFireApiService(@Named("fire") retrofit: Retrofit): FireApiService {
        return retrofit.create(FireApiService::class.java)
    }

    @Singleton
    @Provides
    fun getFireRepository(
        apiService: FireApiService,
        fireDao: FireDao
    ): FireRepository {
        return FireRepository.getInstance(apiService, fireDao)
    }
}