package com.openclassrooms.realestatemanager.domain.models

/**
 * Create by Emmanuel gabé on 21/07/2021.
 */
enum class RealEstateType(val type: String) {
    APARTMENT("Apartment"),
    HOUSE("House"),
    ESTATE("Estate"),
    CASTLE("Castle"),
    PENTHOUSE("Penthouse"),
    STUDIO("Studio"),
    VILLA("Villa"),
    TOWNHOUSE("Townhouse"),
    BUILDING_PLOT("Building plot");

    override fun toString(): String {
        return type
    }}