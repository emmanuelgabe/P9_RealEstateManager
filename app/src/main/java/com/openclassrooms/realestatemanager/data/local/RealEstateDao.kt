package com.openclassrooms.realestatemanager.data.local

import androidx.room.*
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface RealEstateDao {

    @Query("SELECT * FROM real_estate_table")
    fun getAllRealEstates(): Flow<List<RealEstateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRealEstate(realEstateEntity: RealEstateEntity)

    @Update
    suspend fun updateRealEstate(realEstateEntity: RealEstateEntity)

    @Query(
        "SELECT * FROM real_estate_table WHERE " +
                "price BETWEEN :minPrice AND :maxPrice " +
                "AND size BETWEEN :minSize AND :maxSize " +
                "AND (entry_date BETWEEN :minEntryDate AND :maxEntryDate) " +
                "AND (sale_date BETWEEN :minSaleDate AND :maxSaleDate) " +
                "AND address LIKE '%' || :city || '%'"
    )
    fun getFilteredRealEstates(
        minPrice: Int,
        maxPrice: Int,
        minSize: Int,
        maxSize: Int,
        minEntryDate: Date,
        maxEntryDate: Date,
        minSaleDate: Date?,
        maxSaleDate: Date,
        city: String
    ): Flow<List<RealEstateEntity>>


    @Query(
        "SELECT * FROM real_estate_table WHERE " +
                "price BETWEEN :minPrice AND :maxPrice " +
                "AND size BETWEEN :minSize AND :maxSize " +
                "AND (entry_date BETWEEN :minEntryDate AND :maxEntryDate) " +
                "AND address LIKE '%' || :city || '%'"
    )
    fun getFilteredRealEstates(
        minPrice: Int,
        maxPrice: Int,
        minSize: Int,
        maxSize: Int,
        minEntryDate: Date,
        maxEntryDate: Date,
        city: String
    ): Flow<List<RealEstateEntity>>
}