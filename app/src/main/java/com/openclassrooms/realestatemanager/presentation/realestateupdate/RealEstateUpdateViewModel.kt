package com.openclassrooms.realestatemanager.presentation.realestateupdate

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.usecase.RealEstateUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealEstateUpdateViewModel @Inject constructor(
    private val realEstateUseCases: RealEstateUseCases
) : ViewModel() {

    private val _realEstate = MutableLiveData<RealEstate>()
    val realEstate: LiveData<RealEstate>
        get() = _realEstate

    private val _photosToDelete = MutableLiveData<MutableList<Uri>>()
    val photosToDelete: LiveData<MutableList<Uri>>
        get() = _photosToDelete

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: UpdateRealEstateEvent) {
        when (event) {
            is UpdateRealEstateEvent.UpdateRealEstate -> {
                viewModelScope.launch {
                    try {
                        realEstateUseCases.updateRealEstate(realEstate.value!!)
                        _eventFlow.emit(UIEvent.ShowSnackbar("The real estate has been correctly updated"))
                        _eventFlow.emit(UIEvent.RealEstateUpdated)
                    } catch (e: Exception) {
                        _eventFlow.emit(UIEvent.ShowSnackbar("Error save $e"))
                        Log.e("ERROR SAVE", "ERROR SAVE $e")
                    }
                }
            }
        }
    }

    fun init(realEstate: RealEstate) {
        _realEstate.value = realEstate
        _photosToDelete.value = arrayListOf()
    }

    fun addPhoto(photo: Photo) {
        _realEstate.value!!.photos.add(photo)
    }

    fun updatePhoto(photo: Photo, index: Int) {
        _realEstate.value!!.photos[index] = photo
    }

    fun removePhoto(photo: Photo) {
        if (!photo.uriIsExternal) {
            _photosToDelete.value!!.add(photo.uri)
        }
        _realEstate.value!!.photos.remove(photo)
    }

    fun updateSaleDate(saledate: String) {
        _realEstate.value = realEstate.value!!.copy(saleDate = saledate)
    }

    sealed class UIEvent {
        object RealEstateUpdated : UIEvent()
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}