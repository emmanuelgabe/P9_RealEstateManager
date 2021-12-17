package com.openclassrooms.realestatemanager.presentation.realestateupdate

sealed class UpdateRealEstateEvent {
    object UpdateRealEstate : UpdateRealEstateEvent()
}