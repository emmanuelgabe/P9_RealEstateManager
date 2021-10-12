package com.openclassrooms.realestatemanager.domain.models

/**
 * Create by Emmanuel gabé on 17/09/2021.
 */
data class Address(
    val streetNumber:Int? = null,
    val streetName: String? = null,
    val postalCode:Int? = null,
    val city:String,
    val country:String? = null
)