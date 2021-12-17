package com.openclassrooms.realestatemanager.data.local

import androidx.room.*
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RealEstateDao {

    @Query("SELECT * FROM real_estate_table")
    fun getAllRealEstates(): Flow<List<RealEstateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRealEstate(realEstateEntity: RealEstateEntity)

    @Update
    suspend fun updateRealEstate(realEstateEntity: RealEstateEntity)
}