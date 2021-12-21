package com.openclassrooms.realestatemanager.di

import android.app.Application
import androidx.room.Room
import com.openclassrooms.realestatemanager.data.local.DataConverter
import com.openclassrooms.realestatemanager.data.local.RealEstateDatabase
import com.openclassrooms.realestatemanager.data.remote.GeoCodingAPI
import com.openclassrooms.realestatemanager.data.repository.RealEstateRepositoryImpl
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.usecase.GetRealEstates
import com.openclassrooms.realestatemanager.domain.usecase.InsertRealEstate
import com.openclassrooms.realestatemanager.domain.usecase.RealEstateUseCases
import com.openclassrooms.realestatemanager.domain.usecase.UpdateRealEstate
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
        db: RealEstateDatabase,
        geoCodingApi: GeoCodingAPI
    ): RealEstateRepository {
        return RealEstateRepositoryImpl(db.realEstateDao, geoCodingApi)
    }

    @Provides
    @Singleton
    fun provideRealEstateDatabase(app: Application): RealEstateDatabase {
        return Room.databaseBuilder(
            app,
            RealEstateDatabase::class.java,
            RealEstateDatabase.DATABASE_NAME
        ).addTypeConverter(DataConverter())
            .build()
    }

    @Provides
    @Singleton
    fun provideRealEstateUseCases(repository: RealEstateRepository): RealEstateUseCases {
        return RealEstateUseCases(
            getRealEstates = GetRealEstates(repository),
            insertRealEstate = InsertRealEstate(repository),
            updateRealEstate = UpdateRealEstate(repository)
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