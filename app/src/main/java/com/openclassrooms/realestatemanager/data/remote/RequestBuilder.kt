package com.openclassrooms.realestatemanager.data.remote

import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.domain.models.Address
import com.openclassrooms.realestatemanager.utils.*

object RequestBuilder {

    fun mapsStaticAPIUrl(lat: Double, lng: Double): String {
        return BASE_URL +
                STATICMAP_PATH +
                "zoom=$MAPS_ZOOM&" +
                "size=$MAPS_SIZE&" +
                "markers=anchor:$MAPS_MARKER_ANCHOR|" +
                "icon:$MAPS_MARKER_ICON|" +
                "$lat,$lng&" +
                "key=${BuildConfig.API_KEY}"
    }

    fun getStringAddress(address: Address): String {
        val s = "${address.streetNumber},${address.streetName},${address.postalCode},${address.city},${address.country}"
        return s.replace(" ", "+")
    }
}