package com.openclassrooms.realestatemanager.data.local

import android.database.Cursor
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity
import com.openclassrooms.realestatemanager.utils.REAL_ESTATE_TABLE_NAME
import kotlinx.coroutines.flow.Flow


@Dao
interface RealEstateDao {

    @Query("SELECT * FROM $REAL_ESTATE_TABLE_NAME")
    fun getAllRealEstates(): Flow<List<RealEstateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRealEstate(realEstateEntity: RealEstateEntity)

    @Update
    suspend fun updateRealEstate(realEstateEntity: RealEstateEntity)

    @RawQuery(observedEntities = [RealEstateEntity::class])
    fun getFilteredRealEstates(query: SupportSQLiteQuery): Flow<List<RealEstateEntity>>

    @Query("SELECT * FROM real_estate_table")
    fun getAllRealEstatesWithCursor(): Cursor
}