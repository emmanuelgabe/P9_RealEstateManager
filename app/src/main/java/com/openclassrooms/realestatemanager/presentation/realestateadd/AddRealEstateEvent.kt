package com.openclassrooms.realestatemanager.presentation.realestateadd

sealed class AddRealEstateEvent {
    object SaveRealEstate: AddRealEstateEvent()
}