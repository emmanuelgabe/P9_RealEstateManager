package com.openclassrooms.realestatemanager.presentation.realestateadd

import com.openclassrooms.realestatemanager.domain.models.RealEstate

sealed class AddRealEstateEvent {
    data class SaveRealEstate(val realEstate: RealEstate) : AddRealEstateEvent()
}