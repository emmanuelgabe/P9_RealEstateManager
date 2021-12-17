package com.openclassrooms.realestatemanager.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Create by Emmanuel gabé on 17/09/2021.
 */
@Parcelize
data class Address(
    var streetNumber: Int?,
    var streetName: String?,
    var postalCode: Int?,
    var city: String?,
    var country: String?
) : Parcelable