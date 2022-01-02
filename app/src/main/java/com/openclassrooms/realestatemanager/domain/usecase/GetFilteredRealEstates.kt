package com.openclassrooms.realestatemanager.domain.usecase

import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateFilter
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import kotlinx.coroutines.flow.Flow

class GetFilteredRealEstates(private val repository: RealEstateRepository) {
    operator fun invoke(realEstateFilter: RealEstateFilter): Flow<List<RealEstate>> {
        return repository.getFilteredRealEstate(realEstateFilter)
    }
}
