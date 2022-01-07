package com.openclassrooms.realestatemanager.di

import android.app.Application
import com.openclassrooms.realestatemanager.data.local.LocalDataSource
import com.openclassrooms.realestatemanager.data.local.RealEstateDatabase
import com.openclassrooms.realestatemanager.data.remote.GeoCodingAPI
import com.openclassrooms.realestatemanager.data.repository.RealEstateRepositoryImpl
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.usecase.*
import com.openclassrooms.realestatemanager.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealEstateModule {

    @Provides
    @Singleton
    fun provideRealEstateRepository(
        geoCodingApi: GeoCodingAPI,
        localDataSource: LocalDataSource
    ): RealEstateRepository {
        return RealEstateRepositoryImpl(geoCodingApi, localDataSource)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        db: RealEstateDatabase
    ): LocalDataSource {
        return LocalDataSource(db.realEstateDao)
    }

    @Provides
    @Singleton
    fun provideRealEstateDatabase(app: Application): RealEstateDatabase {
        return RealEstateDatabase.getRealEstateDatabase(app.applicationContext)
    }

    @Provides
    @Singleton
    fun provideRealEstateUseCases(repository: RealEstateRepository): RealEstateUseCases {
        return RealEstateUseCases(
            getRealEstates = GetRealEstates(repository),
            insertRealEstate = InsertRealEstate(repository),
            updateRealEstate = UpdateRealEstate(repository),
            getFilteredRealEState = GetFilteredRealEstates(repository),
            getGeocoding = GetGeocoding(repository)
        )
    }

    @Provides
    @Singleton
    fun provideGetRealEstatesUseCase(repository: RealEstateRepository): GetRealEstates {
        return GetRealEstates(repository)
    }

    @Provides
    @Singleton
    fun provideGeoCodingApi(): GeoCodingAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoCodingAPI::class.java)
    }
}