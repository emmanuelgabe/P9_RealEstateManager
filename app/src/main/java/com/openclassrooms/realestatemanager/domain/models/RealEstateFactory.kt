package com.openclassrooms.realestatemanager.domain.models

import android.net.Uri
import com.openclassrooms.realestatemanager.domain.utils.DateUtil
import com.openclassrooms.realestatemanager.utils.DATE_FORMAT_ENTRY_DATE
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by Emmanuel gabé on 17/09/2021.
 */
class RealEstateFactory
constructor(private val dateUtil: DateUtil = DateUtil(SimpleDateFormat(DATE_FORMAT_ENTRY_DATE))) {
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
        entryDate: String? = null,
        saleDate: String? = null,
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
            entryDate = entryDate ?: dateUtil.getCurrentTimestamp(),
            saleDate = saleDate ?: "",
            realEstateAgent = realEstateAgent,
            nearbyInterest = nearbyInterest ?: mutableListOf<NearbyInterest>(),
            photos = createPhotoList(photosUri ?: emptyList(), photosDescription ?: emptyList())
        )
    }

    private fun createPhotoList(
        photosUri: List<Uri>, photosDescription: List<String>
    ): MutableList<Photo> {
        val list = mutableListOf<Photo>()
        for ((index, photoUri) in photosUri.withIndex()) {
            val photo = Photo(photoUri, photosDescription.get(index), false)
            list.add(photo)
        }
        return list
    }
}
