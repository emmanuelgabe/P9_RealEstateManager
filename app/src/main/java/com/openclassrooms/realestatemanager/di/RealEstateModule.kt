package com.openclassrooms.realestatemanager.di

import android.app.Application
import androidx.room.Room
import com.openclassrooms.realestatemanager.data.local.DataConverter
import com.openclassrooms.realestatemanager.data.local.RealEstateDatabase
import com.openclassrooms.realestatemanager.data.repository.RealEstateRepositoryImpl
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.usecase.GetRealEstates
import com.openclassrooms.realestatemanager.domain.usecase.InsertRealEstate
import com.openclassrooms.realestatemanager.domain.usecase.RealEstateUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealEstateModule {

    @Provides
    @Singleton
    fun provideRealEstateRepository(db: RealEstateDatabase): RealEstateRepository {
        return RealEstateRepositoryImpl(db.realEstateDao)
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
            insertRealEstate = InsertRealEstate(repository)
        )
    }

    @Provides
    @Singleton
    fun provideGetRealEstatesUseCase(repository: RealEstateRepository): GetRealEstates {
        return GetRealEstates(repository)
    }
}