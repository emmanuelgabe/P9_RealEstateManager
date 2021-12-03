package com.openclassrooms.realestatemanager.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.usecase.RealEstateUseCases
import com.openclassrooms.realestatemanager.domain.utils.Resource
import com.openclassrooms.realestatemanager.presentation.realestatelist.RealEstateListViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject constructor(
    private val realEstateUseCases: RealEstateUseCases
) : ViewModel() {
    private val _listViewState = MutableLiveData(RealEstateListViewState())
    val listViewState: LiveData<RealEstateListViewState>
        get() = _listViewState

    private var getRealEstatesJob: Job? = null
    var realEstateDetailIsDisplay = false

    init {
        getRealEstate()
    }

    fun getRealEstate() {
        getRealEstatesJob?.cancel()
        getRealEstatesJob = viewModelScope.launch {
            realEstateUseCases.getRealEstates()
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _listViewState.value =
                                RealEstateListViewState(realEstates = result.data ?: emptyList())
                        }
                    }
                }.launchIn(this)
        }
    }
}