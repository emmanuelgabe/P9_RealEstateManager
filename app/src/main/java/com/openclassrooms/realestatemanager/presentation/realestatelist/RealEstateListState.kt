package com.openclassrooms.realestatemanager.presentation.realestatelist

import android.provider.ContactsContract
import com.openclassrooms.realestatemanager.domain.models.RealEstate

data class RealEstateListState(val realEstates: List<RealEstate>? = emptyList(),)
// TODO add filter state