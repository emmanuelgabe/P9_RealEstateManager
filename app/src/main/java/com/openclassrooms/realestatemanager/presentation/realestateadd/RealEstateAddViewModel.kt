package com.openclassrooms.realestatemanager.presentation.realestateadd

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateFactory
import com.openclassrooms.realestatemanager.domain.models.RealEstateStatus
import com.openclassrooms.realestatemanager.domain.usecase.RealEstateUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealEstateAddViewModel @Inject constructor(
    private val realEstateUseCases: RealEstateUseCases
) : ViewModel() {

    private val _realEstate = MutableLiveData<RealEstate>()
    val realEstate: LiveData<RealEstate>
        get() = _realEstate

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: AddRealEstateEvent) {
        when (event) {
            is AddRealEstateEvent.SaveRealEstate -> {
                viewModelScope.launch {
                    try {
                        realEstateUseCases.insertRealEstate(realEstate.value!!)
                        _eventFlow.emit(UIEvent.ShowSnackBar("real estate added"))
                        _eventFlow.emit(UIEvent.RealEstateAdded)
                    } catch (e: Exception) {
                        _eventFlow.emit(UIEvent.ShowSnackBar("Error save $e"))
                        Log.e("ERROR SAVE", "ERROR SAVE $e")
                    }
                }
            }
        }
    }

    init {
        _realEstate.value =
            RealEstateFactory().createRealEstate(
                status = RealEstateStatus.AVAILABLE)
    }

    fun removePhoto(photo: Photo) {
        _realEstate.value!!.photos.remove(photo)
    }

    fun addPhoto(photo: Photo) {
        _realEstate.value!!.photos.add(photo)
    }

    fun updatePhoto(photo: Photo, index: Int) {
        _realEstate.value!!.photos[index] = photo
    }

    sealed class UIEvent {
        object RealEstateAdded : UIEvent()
        data class ShowSnackBar(val message: String) : UIEvent()
    }
}