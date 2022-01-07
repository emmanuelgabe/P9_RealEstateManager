package com.openclassrooms.realestatemanager.domain.models

import com.openclassrooms.realestatemanager.presentation.dialog.FilterBottomSheetDialogViewState

data class RealEstateFilter(
    val minSize: Int?,
    val maxSize: Int?,
    val minPrice: Int?,
    val maxPrice: Int?,
    val minPhoto: Int?,
    val maxPhoto: Int?,
    val minSaleDate: Long?,
    val maxSaleDate: Long?,
    val minEntryDate: Long?,
    val maxEntryDate: Long?,
    val nearbyInterest: List<NearbyInterest>,
    val city: String?,
) {
    companion object {
        fun createFilterFromState(filterBottomSheetDialogViewState: FilterBottomSheetDialogViewState): RealEstateFilter {
            return RealEstateFilter(
                minSize = filterBottomSheetDialogViewState.minMaxSize.value[0].toInt(),
                maxSize = filterBottomSheetDialogViewState.minMaxSize.value[1].toInt(),
                minPrice = filterBottomSheetDialogViewState.minMaxPrice.value[0].toInt(),
                maxPrice = filterBottomSheetDialogViewState.minMaxPrice.value[1].toInt(),
                minPhoto = filterBottomSheetDialogViewState.minMaxPhoto.value[0].toInt(),
                maxPhoto = filterBottomSheetDialogViewState.minMaxPhoto.value[1].toInt(),
                minSaleDate = filterBottomSheetDialogViewState.minSaleDate.value?.time,
                maxSaleDate = filterBottomSheetDialogViewState.maxSaleDate.value?.time,
                minEntryDate = filterBottomSheetDialogViewState.minEntryDate.value?.time,
                maxEntryDate = filterBottomSheetDialogViewState.maxEntryDate.value?.time,
                nearbyInterest = filterBottomSheetDialogViewState.nearbyInterest,
                city = filterBottomSheetDialogViewState.city.value
            )
        }
    }
}