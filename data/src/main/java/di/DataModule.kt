package di

import android.app.Application
import com.example.data.database.WeatherDatabase
import com.example.data.database.dao.CityDao
import com.example.data.database.dao.WeatherDao
import com.example.data.network.KtorClient
import com.example.data.repository.FavouriteRepositoryImpl
import com.example.data.repository.ForecastRepositoryImpl
import com.example.data.repository.SearchRepositoryImpl
import com.example.domain.repository.FavouriteRepository
import com.example.domain.repository.ForecastRepository
import com.example.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient

@Module
interface DataModule {

    @Binds
    fun bindForecastRepository(impl: ForecastRepositoryImpl): ForecastRepository

    @Binds
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    fun bindFavouriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    companion object {

        @Provides
        fun provideCityDao(application: Application): CityDao = WeatherDatabase
            .getInstance(application).cityDao()

        @Provides
        fun provideHourlyForecastDao(application: Application): WeatherDao = WeatherDatabase
            .getInstance(application).hourlyForecastDao()

        @Provides
        fun provideKtorClient(): HttpClient = KtorClient.client

    }

}