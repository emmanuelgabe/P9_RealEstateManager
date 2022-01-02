package com.openclassrooms.realestatemanager.domain.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Create by Emmanuel gabé on 21/07/2021.
 */

@Parcelize
data class RealEstate(
    val id: String,
    var type: RealEstateType?,
    var status: RealEstateStatus,
    var price: Int?,
    var size: Int?,
    var room: Int?,
    var description: String?,
    val address: Address?,
    val photos: MutableList<Photo>,
    var mapPhoto: Uri?,
    var lat: Double?,
    var lng: Double?,
    val entryDate: Date,
    var saleDate: Date?,
    var realEstateAgent: String?,
    var nearbyInterest: MutableList<NearbyInterest>
) : Parcelable