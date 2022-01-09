package com.openclassrooms.realestatemanager.presentation

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.models.NearbyInterest
import com.openclassrooms.realestatemanager.domain.models.RealEstateFilter
import com.openclassrooms.realestatemanager.presentation.dialog.FilterBottomSheetDialogViewState
import com.openclassrooms.realestatemanager.utils.MAX_ENTRY_DATE_TAG
import com.openclassrooms.realestatemanager.utils.MAX_SALE_DATE_TAG
import com.openclassrooms.realestatemanager.utils.MIN_ENTRY_DATE_TAG
import com.openclassrooms.realestatemanager.utils.MIN_SALE_DATE_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject constructor() : ViewModel() {

    private var _realEstateFilter = MutableStateFlow(FilterBottomSheetDialogViewState())
    val realEstateFilter = _realEstateFilter.asStateFlow()

    private val _uiEventFlow = MutableSharedFlow<UIEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    private val _filterEventFlow = MutableSharedFlow<FilterEvent>()
    val filterEventFlow = _filterEventFlow.asSharedFlow()

    private var _lastKnownLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    val lastKnownLocation = _lastKnownLocation.asStateFlow()

    private var getRealEstatesJob: Job? = null

    fun updateDate(date: Date?, tag: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            when (tag) {
                MIN_ENTRY_DATE_TAG -> {
                    if (_realEstateFilter.value.maxEntryDate.value != null && date != null && date.time > _realEstateFilter.value.maxEntryDate.value!!.time) {
                        _uiEventFlow.emit(UIEvent.ShowToast("The minimum date cannot be later than the maximum date"))
                    } else {
                        _realEstateFilter.value.minEntryDate.value = date
                    }
                }
                MAX_ENTRY_DATE_TAG -> {
                    if (_realEstateFilter.value.minEntryDate.value != null && date != null && date.time < _realEstateFilter.value.minEntryDate.value!!.time) {
                        _uiEventFlow.emit(UIEvent.ShowToast("The maximum date cannot be older than the minimum date"))
                    } else {
                        _realEstateFilter.value.maxEntryDate.value = date
                    }
                }
                MIN_SALE_DATE_TAG -> {
                    if (_realEstateFilter.value.maxSaleDate.value != null && date != null && date.time > _realEstateFilter.value.maxSaleDate.value!!.time) {
                        _uiEventFlow.emit(UIEvent.ShowToast("The minimum date cannot be later than the maximum date"))
                    } else {
                        _realEstateFilter.value.minSaleDate.value = date
                    }
                }
                MAX_SALE_DATE_TAG -> {
                    if (_realEstateFilter.value.minSaleDate.value != null && date != null && date.time < _realEstateFilter.value.minSaleDate.value!!.time) {
                        _uiEventFlow.emit(UIEvent.ShowToast("The maximum date cannot be older than the minimum date"))
                    } else {
                        _realEstateFilter.value.maxSaleDate.value = date
                    }
                }
            }
        }
    }

    fun applyFilter() {
        getRealEstatesJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        getRealEstatesJob = viewModelScope.launch {
            _filterEventFlow.emit(
                FilterEvent.ApplyFilter(
                    RealEstateFilter.createFilterFromState(
                        _realEstateFilter.value
                    )
                )
            )
        }
    }

    fun resetFilter() {
        _realEstateFilter.value = FilterBottomSheetDialogViewState()
        getRealEstatesJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        getRealEstatesJob = viewModelScope.launch {
            _uiEventFlow.emit(UIEvent.ResetNearByInterestList)
        }
    }

    fun updateNearByInterest(nearbyInterests: List<NearbyInterest>) {
        _realEstateFilter.value.nearbyInterest.clear()
        _realEstateFilter.value.nearbyInterest.addAll(nearbyInterests)
    }

    fun saveLocation(location: Location?) {
        _lastKnownLocation.value = location
    }

    sealed class UIEvent {
        data class ShowToast(val message: String) : UIEvent()
        object ResetNearByInterestList : UIEvent()
    }

    sealed class FilterEvent {
        data class ApplyFilter(val realEstateFilter: RealEstateFilter) :
            MainActivityViewModel.FilterEvent()
    }
}