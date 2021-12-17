package com.openclassrooms.realestatemanager.domain.models

enum class
NearbyInterest(val nearbyInterest: String) {
    SCHOOL("School"),
    MONUMENT("Monument"),
    PARK("Park"),
    RESTAURANT("Restaurant"),
    BANK("Bank"),
    TRAINSTATION("Train station"),
    SHOPPINGMALL("Shopping mall");

    override fun toString(): String {
        return nearbyInterest
    }
}