package com.openclassrooms.realestatemanager.domain.models

import android.net.Uri

/**
 * Create by Emmanuel gabé on 21/07/2021.
 */
data class RealEstate(
    val id: String,
    val type: RealEstateType,
    val status: RealEstateStatus,
    val price: Int,
    val size: Int,
    val room: Int,
    val description: String,
    val address: Address,
    val photoDescription: List<String>? = null,
    val photoUri: List<Uri>? = null,
    val mapPhoto: Uri? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val entryDate: String,
    val saleDate: String? = null
)