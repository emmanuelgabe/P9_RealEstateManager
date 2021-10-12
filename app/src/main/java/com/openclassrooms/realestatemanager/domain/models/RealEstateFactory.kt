package com.openclassrooms.realestatemanager.domain.models

import android.net.Uri
import com.openclassrooms.realestatemanager.domain.utils.DateUtil
import java.util.*

/**
 * Create by Emmanuel gabé on 17/09/2021.
 */
class RealEstateFactory constructor(private val dateUtil: DateUtil) {
    fun createRealEstate(
        id: String? = null,
        name: String,
        type: RealEstateType,
        price: Int,
        size: Int,
        room: Int,
        description: String,
        photoUri: List<Uri>? = null,
        photoDescription: List<String>? = null,
        streetNumber: Int? = null,
        streetName: String? = null,
        postalCode: Int? = null,
        city: String,
        country: String? = null,
        status: RealEstateStatus,
        mapPhoto: Uri? = null,
        lat: Double,
        lng: Double,
        entryDate: String? = null,
        saleDate: String? = null
    ): RealEstate {
        return RealEstate(
            id = id ?: UUID.randomUUID().toString(),
            name = name,
            type = type,
            price = price,
            size = size,
            room = room,
            description = description,
            photoUri = photoUri,
            photoDescription = photoDescription,
            address = Address(
                streetNumber = streetNumber,
                streetName = streetName,
                postalCode = postalCode,
                city = city,
                country = country
            ),
            status = status,
            mapPhoto = mapPhoto,
            lat = lat,
            lng = lng,
            entryDate = entryDate ?: dateUtil.getCurrentTimestamp(),
            saleDate = saleDate
        )
    }
}