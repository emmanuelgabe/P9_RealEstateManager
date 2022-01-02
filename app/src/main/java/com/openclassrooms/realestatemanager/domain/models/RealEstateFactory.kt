package com.openclassrooms.realestatemanager.domain.models

import android.net.Uri
import java.util.*

/**
 * Create by Emmanuel gabé on 17/09/2021.
 */
class RealEstateFactory {
    fun createRealEstate(
        id: String? = null,
        type: RealEstateType? = null,
        price: Int? = null,
        size: Int? = null,
        room: Int? = null,
        description: String? = null,
        streetNumber: Int? = null,
        streetName: String? = null,
        postalCode: Int? = null,
        city: String? = null,
        country: String? = null,
        status: RealEstateStatus,
        mapPhoto: Uri? = null,
        lat: Double? = null,
        lng: Double? = null,
        entryDate: Date? = null,
        saleDate: Date? = null,
        realEstateAgent: String? = null,
        nearbyInterest: MutableList<NearbyInterest>? = null,
        photosUri: List<Uri>? = null,
        photosDescription: List<String>? = null
    ): RealEstate {
        return RealEstate(
            id = id ?: UUID.randomUUID().toString(),
            type = type,
            price = price,
            size = size,
            room = room,
            description = description,
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
            entryDate = entryDate ?: Calendar.getInstance().time,
            saleDate = saleDate,
            realEstateAgent = realEstateAgent,
            nearbyInterest = nearbyInterest ?: mutableListOf(),
            photos = createPhotoList(photosUri ?: emptyList(), photosDescription ?: emptyList())
        )
    }

    private fun createPhotoList(
        photosUri: List<Uri>, photosDescription: List<String>
    ): MutableList<Photo> {
        val list = mutableListOf<Photo>()
        for ((index, photoUri) in photosUri.withIndex()) {
            val photo = Photo(photoUri, photosDescription[index], false)
            list.add(photo)
        }
        return list
    }
}
