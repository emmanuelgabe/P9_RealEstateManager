package com.openclassrooms.realestatemanager.domain.usecase

import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository

class InsertRealEstate(private val repository: RealEstateRepository) {
    suspend operator fun invoke(realEstate: RealEstate) {
        repository.insertRealEstate(realEstate)
    }
}