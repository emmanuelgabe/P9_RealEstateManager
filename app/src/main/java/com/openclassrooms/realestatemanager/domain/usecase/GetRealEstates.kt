package com.openclassrooms.realestatemanager.domain.usecase

import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import kotlinx.coroutines.flow.Flow

class GetRealEstates(private val repository: RealEstateRepository) {

    operator fun invoke():  Flow<List<RealEstate>>{
        return repository.getAllRealEstate()
    }
}