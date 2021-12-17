package com.openclassrooms.realestatemanager.data.repository

import com.openclassrooms.realestatemanager.data.local.RealEstateDao
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealEstateRepositoryImpl(private val realEstateDao: RealEstateDao) : RealEstateRepository {

    override fun getAllRealEstate(): Flow<List<RealEstate>> {
        return realEstateDao.getAllRealEstates().map { realEstates ->
            realEstates.map { it.entityToRealEstate() }
        }
    }

    override suspend fun insertRealEstate(realEstate: RealEstate) {
        realEstateDao.insertRealEstate(RealEstateEntity.realEstateToRealEStateEntity(realEstate))
    }

    override suspend fun updateRealEstate(realEstate: RealEstate) {
        realEstateDao.updateRealEstate(RealEstateEntity.realEstateToRealEStateEntity(realEstate))
    }
}