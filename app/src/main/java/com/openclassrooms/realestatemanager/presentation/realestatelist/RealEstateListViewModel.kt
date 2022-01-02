package com.openclassrooms.realestatemanager.presentation.realestatelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateFilter
import com.openclassrooms.realestatemanager.domain.usecase.RealEstateUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealEstateListViewModel @Inject constructor(
    private val realEstateUseCases: RealEstateUseCases
) : ViewModel() {

    private val _realEstates = MutableLiveData<List<RealEstate>>()
    val realEstates: LiveData<List<RealEstate>>
        get() = _realEstates

    private var getRealEstatesJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getRealEstate()
    }

    private fun getRealEstate() {
        getRealEstatesJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        getRealEstatesJob = viewModelScope.launch {
            realEstateUseCases.getRealEstates().collectLatest { realEstates ->
                _realEstates.value = realEstates
                _eventFlow.emit(UiEvent.SubmitList(realEstates))
            }
        }
    }

    fun updateList(realEstateFilter: RealEstateFilter) {
        getRealEstatesJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        getRealEstatesJob = viewModelScope.launch {
            realEstateUseCases.getFilteredRealEState(
                realEstateFilter
            ).collectLatest { realEstates ->
                _realEstates.value = realEstates
                _eventFlow.emit(UiEvent.SubmitList(realEstates))
            }
        }
    }

    sealed class UiEvent {
        data class SubmitList(val realEstateList: List<RealEstate>) : UiEvent()
    }
}