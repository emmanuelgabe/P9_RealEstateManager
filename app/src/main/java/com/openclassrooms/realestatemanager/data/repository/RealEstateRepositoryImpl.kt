package com.openclassrooms.realestatemanager.data.repository

import android.location.Location
import com.openclassrooms.realestatemanager.data.local.LocalDataSource
import com.openclassrooms.realestatemanager.data.local.entity.RealEstateEntity
import com.openclassrooms.realestatemanager.data.remote.AddressPositionNotFoundException
import com.openclassrooms.realestatemanager.data.remote.GeoCodingAPI
import com.openclassrooms.realestatemanager.data.remote.RemoteErrors.ERROR_POSITION_NOT_FOUND
import com.openclassrooms.realestatemanager.data.remote.RequestBuilder.getStringAddress
import com.openclassrooms.realestatemanager.domain.models.Address
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateFilter
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.utils.MAX_PHOTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealEstateRepositoryImpl(
    private val geoCodingApi: GeoCodingAPI,
    private val localDataSource: LocalDataSource
) : RealEstateRepository {

    override fun getAllRealEstate(): Flow<List<RealEstate>> {
        return localDataSource.getAllRealEstates().map { realEstates ->
            realEstates.map { it.entityToRealEstate() }
        }
    }

    override suspend fun insertRealEstate(realEstate: RealEstate) {
        localDataSource.insertRealEstate(RealEstateEntity.realEstateToRealEStateEntity(realEstate))
    }

    override suspend fun updateRealEstate(realEstate: RealEstate) {
        localDataSource.updateRealEstate(RealEstateEntity.realEstateToRealEStateEntity(realEstate))
    }

    override fun getFilteredRealEstate(realEstateFilter: RealEstateFilter): Flow<List<RealEstate>> {
        return localDataSource.getFilteredRealEstates(realEstateFilter)
            .map { realEstatesEntityList ->
                realEstatesEntityList.filter {
                    filterPhotoCount(it.photoUri?.size, realEstateFilter)
                }.map { it.entityToRealEstate() }
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

    //  TODO("Refactor out of repository")
    private fun filterPhotoCount(photoCount: Int?, realEstateFilter: RealEstateFilter): Boolean {
        var conditionIsValid = false
        var photosCount = photoCount
        if (photoCount == null) photosCount = 0
        if (realEstateFilter.minPhoto == null && realEstateFilter.maxPhoto == null) {
            conditionIsValid = true
        }
        if ((realEstateFilter.minPhoto != null && photosCount!! >= realEstateFilter.minPhoto)
            && (realEstateFilter.maxPhoto != null && photosCount <= realEstateFilter.maxPhoto || realEstateFilter.maxPhoto == MAX_PHOTO)
        ) {
            conditionIsValid = true
        }
        return conditionIsValid
    }
}