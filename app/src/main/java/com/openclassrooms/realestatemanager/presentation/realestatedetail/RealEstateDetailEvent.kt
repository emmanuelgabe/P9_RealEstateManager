package com.openclassrooms.realestatemanager.presentation.realestatedetail


sealed class RealEstateDetailEvent {
    object ExpandedCardTouch : RealEstateDetailEvent()
}