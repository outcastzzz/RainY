package com.example.rainy.di

import android.content.Context
import com.example.rainy.data.database.WeatherDatabase
import com.example.rainy.data.database.dao.CityDao
import com.example.rainy.data.network.KtorClient
import com.example.rainy.data.repository.FavouriteRepositoryImpl
import com.example.rainy.data.repository.ForecastRepositoryImpl
import com.example.rainy.data.repository.SearchRepositoryImpl
import com.example.rainy.domain.repository.FavouriteRepository
import com.example.rainy.domain.repository.ForecastRepository
import com.example.rainy.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindForecastRepository(impl: ForecastRepositoryImpl): ForecastRepository

    @Binds
    @ApplicationScope
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    @ApplicationScope
    fun bindFavouriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideCityDao(context: Context): CityDao = WeatherDatabase
            .getInstance(context).cityDao()

        @Provides
        @ApplicationScope
        fun provideKtorClient(): HttpClient = KtorClient.client

    }

}