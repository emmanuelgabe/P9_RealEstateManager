package com.openclassrooms.realestatemanager.data.remote

import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.domain.models.Address
import com.openclassrooms.realestatemanager.utils.*
import okhttp3.Request

object RequestBuilder {

    fun mapsStaticAPIRequest(address: Address): Request {
        return Request.Builder().url(mapsStaticAPIUrl(address)).build()
    }

    fun mapsStaticAPIUrl(address: Address): String {
        val url = BASE_URL +
                REQUEST_PATH +
                "zoom=$MAPS_ZOOM&" +
                "size=$MAPS_SIZE&" +
                "markers=anchor:$MAPS_MARKER_ANCHOR|" +
                "icon:$MAPS_MARKER_ICON|" +
                "${address.streetNumber},${address.streetName},${address.postalCode},${address.city},${address.country}&" +
                "key=${BuildConfig.API_KEY}"
        return url
    }
}