package com.openclassrooms.realestatemanager.domain.usecase

data class RealEstateUseCases(
    val getRealEstates: GetRealEstates,
    val insertRealEstate: InsertRealEstate,
    val updateRealEstate: UpdateRealEstate,
    val getFilteredRealEState: GetFilteredRealEstates,
    val getGeocoding: GetGeocoding
)