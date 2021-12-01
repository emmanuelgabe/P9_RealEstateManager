package com.openclassrooms.realestatemanager.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.usecase.RealEstateUseCases
import com.openclassrooms.realestatemanager.domain.utils.Resource
import com.openclassrooms.realestatemanager.presentation.realestatelist.RealEstateListState
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
    private val _state = mutableStateOf(RealEstateListState())
    val state: State<RealEstateListState> = _state

    private var getRealEstatesJob: Job? = null

    init {
        getRealEstate()
    }

    private fun getRealEstate() {
        getRealEstatesJob?.cancel()
        getRealEstatesJob = viewModelScope.launch {
            realEstateUseCases.getRealEstates()
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                realEstates = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }
}