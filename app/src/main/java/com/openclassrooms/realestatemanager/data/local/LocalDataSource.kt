package com.openclassrooms.realestatemanager.data.local

import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity
import com.openclassrooms.realestatemanager.domain.models.RealEstateFilter
import com.openclassrooms.realestatemanager.utils.QueryBuilder
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val realEstateDao: RealEstateDao) {

    fun getAllRealEstates(): Flow<List<RealEstateEntity>> {
        return realEstateDao.getAllRealEstates()
    }

    fun getFilteredRealEstates(realEstateFilter: RealEstateFilter): Flow<List<RealEstateEntity>> {
        return realEstateDao.getFilteredRealEstates(
            QueryBuilder.getQueryFilteredRealEstate(
                realEstateFilter
            )
        )
    }

    suspend fun insertRealEstate(realEstateEntity: RealEstateEntity) =
        realEstateDao.insertRealEstate(realEstateEntity)

    suspend fun updateRealEstate(realEstateEntity: RealEstateEntity) =
        realEstateDao.updateRealEstate(realEstateEntity)

}
