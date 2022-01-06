package com.openclassrooms.realestatemanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity
import com.openclassrooms.realestatemanager.utils.DATABASE_NAME

@Database(entities = [RealEstateEntity::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class RealEstateDatabase : RoomDatabase() {
    abstract val realEstateDao: RealEstateDao

    companion object {
        @Volatile
        private var INSTANCE: RealEstateDatabase? = null
        fun getRealEstateDatabase(context: Context): RealEstateDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RealEstateDatabase::class.java,
                    DATABASE_NAME
                ).addTypeConverter(DataConverter())
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}