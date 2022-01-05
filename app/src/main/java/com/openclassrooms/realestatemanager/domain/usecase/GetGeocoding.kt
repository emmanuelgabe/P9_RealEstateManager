package com.openclassrooms.realestatemanager.domain.usecase

import android.location.Location
import com.openclassrooms.realestatemanager.domain.models.Address
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository

class GetGeocoding(private val repository: RealEstateRepository) {
    suspend operator fun invoke(address: Address): Location {
        return repository.getGeocoding(address)
    }
}