package com.openclassrooms.realestatemanager.presentation.realestatelist

import android.provider.ContactsContract
import com.openclassrooms.realestatemanager.domain.models.RealEstate

data class RealEstateListViewState(
    val realEstates: List<RealEstate> = emptyList()
)
// TODO add filter state