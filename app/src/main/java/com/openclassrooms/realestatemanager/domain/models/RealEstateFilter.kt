package com.openclassrooms.realestatemanager.domain.models

import com.openclassrooms.realestatemanager.presentation.dialog.FilterBottomSheetDialogViewState
import com.openclassrooms.realestatemanager.utils.MAX_PRICE
import com.openclassrooms.realestatemanager.utils.MAX_SIZE
import java.util.*

data class RealEstateFilter(
    val minSize: Int,
    val maxSize: Int,
    val minPrice: Int,
    val maxPrice: Int,
    val minPhoto: Int,
    val maxPhoto: Int,
    val minSaleDate: Date?,
    val maxSaleDate: Date?,
    val minEntryDate: Date,
    val maxEntryDate: Date,
    val nearbyInterest: List<NearbyInterest>,
    val city: String,
) {
    companion object {
        fun createFilterFromState(filterBottomSheetDialogViewState: FilterBottomSheetDialogViewState): RealEstateFilter {
            var maxSize = filterBottomSheetDialogViewState.minMaxSize.value[1].toInt()
            if (maxSize == MAX_SIZE) {
                maxSize = 100000
            }
            var maxPrice = filterBottomSheetDialogViewState.minMaxPrice.value[1].toInt()
            if (maxPrice == MAX_PRICE) {
                maxPrice = 100000000
            }
            var maxPhoto = filterBottomSheetDialogViewState.minMaxPhoto.value[1].toInt()
            if (maxPhoto == 5) {
                maxPhoto = 100
            }
            var city = filterBottomSheetDialogViewState.city.value
            city = if (city.isNullOrBlank()) {
                ""
            } else {
                "\"city\":\"${filterBottomSheetDialogViewState.city.value}\""
            }
            return RealEstateFilter(
                minSize = filterBottomSheetDialogViewState.minMaxSize.value[0].toInt(),
                maxSize = maxSize,
                minPrice = filterBottomSheetDialogViewState.minMaxPrice.value[0].toInt(),
                maxPrice = maxPrice,
                minPhoto = filterBottomSheetDialogViewState.minMaxPhoto.value[0].toInt(),
                maxPhoto = maxPhoto,
                minSaleDate = filterBottomSheetDialogViewState.minSaleDate.value,
                maxSaleDate = filterBottomSheetDialogViewState.maxSaleDate.value,
                minEntryDate = filterBottomSheetDialogViewState.minEntryDate.value ?: Date(0),
                maxEntryDate = filterBottomSheetDialogViewState.maxEntryDate.value
                    ?: Date(Long.MAX_VALUE),
                nearbyInterest = filterBottomSheetDialogViewState.nearbyInterest,
                city = city
            )
        }
    }
}