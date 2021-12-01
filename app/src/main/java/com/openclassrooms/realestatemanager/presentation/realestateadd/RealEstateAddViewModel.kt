package com.openclassrooms.realestatemanager.presentation.realestateadd

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.usecase.RealEstateUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealEstateAddViewModel @Inject constructor(
    private val realEstateUseCases: RealEstateUseCases
) : ViewModel() {

    fun onEvent(event: AddRealEstateEvent) {
        when (event) {
            is AddRealEstateEvent.SaveRealEstate -> {
                viewModelScope.launch {
                    try {
                        realEstateUseCases.insertRealEstate(event.realEstate)
                    } catch (e: Exception) {
                        Log.e("ERROR SAVE", "ERROR SAVE $e")
                    }
                }
            }
        }
    }
}