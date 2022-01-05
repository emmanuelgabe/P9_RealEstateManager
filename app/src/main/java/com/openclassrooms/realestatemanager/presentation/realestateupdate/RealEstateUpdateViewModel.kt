package com.openclassrooms.realestatemanager.presentation.realestateupdate

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.remote.AddressPositionNotFoundException
import com.openclassrooms.realestatemanager.data.remote.RemoteConstants
import com.openclassrooms.realestatemanager.data.remote.RemoteErrors
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.usecase.RealEstateUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import retrofit2.HttpException
import java.io.IOException
import java.util.*
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
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        updateGeocoding()
                        realEstateUseCases.updateRealEstate(realEstate.value!!)
                        if (realEstate.value!!.lat != null && realEstate.value!!.lng != null) {
                            _eventFlow.emit(UIEvent.ShowSnackBar("The real estate has been correctly updated"))
                        }
                        _eventFlow.emit(UIEvent.RealEstateUpdated)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        _eventFlow.emit(UIEvent.ShowSnackBar("Error save $e"))
                        Log.e("ERROR SAVE", "ERROR SAVE $e")
                    }
                }
            }
        }
    }

    private suspend fun updateGeocoding() {
        try {
            withTimeout(RemoteConstants.NETWORK_TIMEOUT) {
                val location = realEstateUseCases.getGeocoding(realEstate.value!!.address!!)
                realEstate.value!!.lat = location.latitude
                realEstate.value!!.lng = location.longitude
            }
        } catch (e: AddressPositionNotFoundException) {
            Log.e("ERROR GEOCODING", "$e")
            _eventFlow.emit(UIEvent.ShowSnackBar(RemoteErrors.ERROR_POSITION_NOT_FOUND))
        } catch (e: HttpException) {
            Log.e("ERROR GEOCODING", "ERROR GEOCODING $e")
            _eventFlow.emit(UIEvent.ShowSnackBar(RemoteErrors.NETWORK_ERROR_UNKNOWN))
        } catch (e: IOException) {
            Log.e("ERROR GEOCODING", "ERROR GEOCODING $e")
            _eventFlow.emit(UIEvent.ShowSnackBar(RemoteErrors.NETWORK_ERROR_CONNECTION))
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

    fun updateSaleDate(saleDate: Date?) {
        _realEstate.value = realEstate.value!!.copy(saleDate = saleDate)
    }

    sealed class UIEvent {
        object RealEstateUpdated : UIEvent()
        data class ShowSnackBar(val message: String) : UIEvent()
    }
}