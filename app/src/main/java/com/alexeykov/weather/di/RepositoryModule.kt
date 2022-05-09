package com.alexeykov.weather.di

import android.content.Context
import com.alexeykov.weather.model.cloud.ApiFactory
import com.alexeykov.weather.model.cloud.WeatherRepository
import com.alexeykov.weather.model.room.CitiesRepository
import com.alexeykov.weather.model.room.cities.RoomCitiesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLocalRepository(@ApplicationContext context: Context): CitiesRepository =
        RoomCitiesRepository(
            citiesDao = StorageModule.provideDatabase(context).getCitiesDao(),
            ioDispatcher = DispatcherModule.provideIODispatcher()
        )

    @Provides
    @Singleton
    fun provideCloudRepository(): WeatherRepository =
        WeatherRepository(
            api = ApiFactory.weatherApi,
            ioDispatcher = DispatcherModule.provideIODispatcher()
        )

}