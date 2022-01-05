package com.openclassrooms.realestatemanager.data.repository

import android.location.Location
import com.openclassrooms.realestatemanager.data.local.RealEstateDao
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity
import com.openclassrooms.realestatemanager.data.remote.AddressPositionNotFoundException
import com.openclassrooms.realestatemanager.data.remote.GeoCodingAPI
import com.openclassrooms.realestatemanager.data.remote.RemoteErrors.ERROR_POSITION_NOT_FOUND
import com.openclassrooms.realestatemanager.data.remote.RequestBuilder.getStringAddress
import com.openclassrooms.realestatemanager.domain.models.Address
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateFilter
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*


class RealEstateRepositoryImpl(
    private val realEstateDao: RealEstateDao, private val geoCodingApi: GeoCodingAPI
) : RealEstateRepository {

    override fun getAllRealEstate(): Flow<List<RealEstate>> {
        return realEstateDao.getAllRealEstates().map { realEstates ->
            realEstates.map { it.entityToRealEstate() }
        }
    }

    override suspend fun insertRealEstate(realEstate: RealEstate) {
   /*     val geoCoding = geoCodingApi.getGeoCoding(getStringAddress(realEstate.address!!))
        realEstate.lat = geoCoding.results[0].geometry.location.lat
        realEstate.lng = geoCoding.results[0].geometry.location.lng*/
        realEstateDao.insertRealEstate(RealEstateEntity.realEstateToRealEStateEntity(realEstate))
    }

    override suspend fun updateRealEstate(realEstate: RealEstate) {
/*        val geoCoding = geoCodingApi.getGeoCoding(getStringAddress(realEstate.address!!))
        realEstate.lat = geoCoding.results[0].geometry.location.lat
        realEstate.lng = geoCoding.results[0].geometry.location.lng*/
        realEstateDao.updateRealEstate(RealEstateEntity.realEstateToRealEStateEntity(realEstate))
    }

    override fun getFilteredRealEstate(realEstateFilter: RealEstateFilter): Flow<List<RealEstate>> {
        if (realEstateFilter.minSaleDate != null || realEstateFilter.maxSaleDate != null) {
            return realEstateDao.getFilteredRealEstates(
                realEstateFilter.minPrice,
                realEstateFilter.maxPrice,
                realEstateFilter.minSize,
                realEstateFilter.maxSize,
                realEstateFilter.minEntryDate,
                realEstateFilter.maxEntryDate,
                realEstateFilter.minSaleDate ?: Date(0),
                realEstateFilter.maxSaleDate ?: Date(Long.MAX_VALUE),
                realEstateFilter.city
            ).map { realEstates ->
                realEstates.map { it.entityToRealEstate() }
            }
        } else return realEstateDao.getFilteredRealEstates(
            realEstateFilter.minPrice,
            realEstateFilter.maxPrice,
            realEstateFilter.minSize,
            realEstateFilter.maxSize,
            realEstateFilter.minEntryDate,
            realEstateFilter.maxEntryDate,
            realEstateFilter.city
        ).map { realEstates ->
            realEstates.map { it.entityToRealEstate() }
        }
    }

    @Throws(AddressPositionNotFoundException::class)
    override suspend fun getGeocoding(address: Address): Location {
        val location = Location("")
        val geoCoding = geoCodingApi.getGeoCoding(getStringAddress(address))
        if (geoCoding.status == "ZERO_RESULTS") {
            throw AddressPositionNotFoundException(ERROR_POSITION_NOT_FOUND)
        }
        location.longitude = geoCoding.results[0].geometry.location.lng
        location.latitude = geoCoding.results[0].geometry.location.lat
        return location
    }
}