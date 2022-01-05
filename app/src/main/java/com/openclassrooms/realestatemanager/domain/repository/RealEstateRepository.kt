package com.openclassrooms.realestatemanager.domain.repository

import android.location.Location
import com.openclassrooms.realestatemanager.domain.models.Address
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateFilter
import kotlinx.coroutines.flow.Flow

interface RealEstateRepository {
    fun getAllRealEstate(): Flow<List<RealEstate>>
    suspend fun insertRealEstate(realEstate: RealEstate)
    suspend fun updateRealEstate(realEstate: RealEstate)
    fun getFilteredRealEstate(realEstateFilter: RealEstateFilter): Flow<List<RealEstate>>
    suspend fun getGeocoding(address:Address): Location
}