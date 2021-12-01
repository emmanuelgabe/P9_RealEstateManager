package com.openclassrooms.realestatemanager.data.repository

import com.openclassrooms.realestatemanager.data.local.RealEstateDao
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RealEstateRepositoryImpl(private val realEstateDao: RealEstateDao) : RealEstateRepository {

    override fun getAllRealEstate(): Flow<Resource<List<RealEstate>>> = flow {
        emit(Resource.Loading())
        val realEstates = realEstateDao.getAllRealEstates().map { it.entityToRealEstate() }
        emit(Resource.Success<List<RealEstate>>(realEstates))
    }

    override suspend fun insertRealEstate(realEstate: RealEstate) {
        realEstateDao.insertRealEstate(RealEstateEntity.realEstateToRealEStateEntity(realEstate))
    }

    override suspend fun updateRealEstate(realEstate: RealEstate) {
        TODO("Not yet implemented")
    }
}