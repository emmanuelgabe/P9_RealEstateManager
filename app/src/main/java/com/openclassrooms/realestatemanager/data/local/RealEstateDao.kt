package com.openclassrooms.realestatemanager.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity

@Dao
interface RealEstateDao {

    @Query("SELECT * FROM real_estate_table")
    suspend fun getAllRealEstates(): List<RealEstateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRealEstate(realEstateEntity: RealEstateEntity)
}