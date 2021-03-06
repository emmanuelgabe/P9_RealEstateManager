package com.openclassrooms.realestatemanager.presentation.realestatedetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RealEstateDetailViewModel : ViewModel() {

    private val _state = mutableStateOf(RealEstateDetailViewState())
    val state: State<RealEstateDetailViewState>
        get() = _state

    fun onEvent(event: RealEstateDetailEvent) {
        when (event) {
            is RealEstateDetailEvent.ExpandedCardTouch -> {
                _state.value = _state.value.copy(cardIsExpanded = !_state.value.cardIsExpanded)
            }
        }
    }
}