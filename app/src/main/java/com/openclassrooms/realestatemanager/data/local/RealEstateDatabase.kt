package com.openclassrooms.realestatemanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity

@Database(entities = [RealEstateEntity::class/*, AddressEntity::class*/], version = 1)
@TypeConverters(DataConverter::class)
abstract class RealEstateDatabase : RoomDatabase() {
    abstract val realEstateDao: RealEstateDao

    companion object {
        const val DATABASE_NAME = "real_estate_db"
    }
}