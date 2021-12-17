package com.openclassrooms.realestatemanager.domain.repository

import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RealEstateRepository {
    fun getAllRealEstate(): Flow<List<RealEstate>>
    suspend fun insertRealEstate(realEstate: RealEstate)
    suspend fun updateRealEstate(realEstate: RealEstate)
}