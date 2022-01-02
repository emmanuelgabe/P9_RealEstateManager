package com.openclassrooms.realestatemanager.presentation.dialog

import com.openclassrooms.realestatemanager.domain.models.NearbyInterest
import com.openclassrooms.realestatemanager.utils.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

data class FilterBottomSheetDialogViewState(
    var minMaxSize: MutableStateFlow<List<Float>> = MutableStateFlow(
        listOf(
            DEFAULT_MIN_SIZE.toFloat(),
            DEFAULT_MAX_SIZE.toFloat()
        )
    ),
    var minMaxPrice: MutableStateFlow<List<Float>> = MutableStateFlow(
        listOf(
            DEFAULT_MIN_PRICE.toFloat(),
            DEFAULT_MAX_PRICE.toFloat()
        )
    ),

    var minMaxPhoto: MutableStateFlow<List<Float>> = MutableStateFlow(
        listOf(
            DEFAULT_MIN_PHOTO.toFloat(),
            DEFAULT_MAX_PHOTO.toFloat()
        )
    ),
    val minSaleDate: MutableStateFlow<Date?> = MutableStateFlow(null),
    val maxSaleDate: MutableStateFlow<Date?> = MutableStateFlow(null),
    val minEntryDate: MutableStateFlow<Date?> = MutableStateFlow(null),
    val maxEntryDate: MutableStateFlow<Date?> = MutableStateFlow(null),
    val city: MutableStateFlow<String?> = MutableStateFlow(null),
    val nearbyInterest: MutableList<NearbyInterest> = mutableListOf()
)