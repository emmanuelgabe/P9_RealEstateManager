package com.openclassrooms.realestatemanager.presentation.realestateadd

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.remote.AddressPositionNotFoundException
import com.openclassrooms.realestatemanager.data.remote.RemoteConstants.NETWORK_TIMEOUT
import com.openclassrooms.realestatemanager.data.remote.RemoteErrors.ERROR_POSITION_NOT_FOUND
import com.openclassrooms.realestatemanager.data.remote.RemoteErrors.NETWORK_ERROR_CONNECTION
import com.openclassrooms.realestatemanager.data.remote.RemoteErrors.NETWORK_ERROR_UNKNOWN
import com.openclassrooms.realestatemanager.domain.models.Photo
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.domain.models.RealEstateFactory
import com.openclassrooms.realestatemanager.domain.models.RealEstateStatus
import com.openclassrooms.realestatemanager.domain.usecase.RealEstateUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException
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
                    getGeocoding()
                    try {
                        realEstateUseCases.insertRealEstate(realEstate.value!!)
                        if (realEstate.value!!.lat != null && realEstate.value!!.lng != null) {
                            _eventFlow.emit(UIEvent.ShowSnackBar("real estate has been correctly added"))
                        }
                        _eventFlow.emit(UIEvent.RealEstateAdded)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        _eventFlow.emit(UIEvent.ShowSnackBar("Error save $e"))
                        Log.e("ERROR SAVE", "ERROR SAVE $e")
                    }
                }
            }
        }
    }

    private suspend fun getGeocoding() {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                val location =
                    realEstateUseCases.getGeocoding(realEstate.value!!.address!!)
                realEstate.value!!.lat = location.latitude
                realEstate.value!!.lng = location.longitude
            }
        } catch (e: AddressPositionNotFoundException) {
            Log.e("ERROR GEOCODING", "$e")
            _eventFlow.emit(UIEvent.ShowSnackBar(ERROR_POSITION_NOT_FOUND))
        } catch (e: HttpException) {
            Log.e("ERROR GEOCODING", "ERROR GEOCODING $e")
            _eventFlow.emit(UIEvent.ShowSnackBar(NETWORK_ERROR_UNKNOWN))
        } catch (e: IOException) {
            Log.e("ERROR GEOCODING", "ERROR GEOCODING $e")
            _eventFlow.emit(UIEvent.ShowSnackBar(NETWORK_ERROR_CONNECTION))
        }
    }

    init {
        _realEstate.value =
            RealEstateFactory().createRealEstate(
                status = RealEstateStatus.AVAILABLE
            )
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