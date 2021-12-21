package com.openclassrooms.realestatemanager.data.remote

import com.openclassrooms.realestatemanager.BuildConfig.API_KEY
import com.openclassrooms.realestatemanager.data.remote.dto.GeoCodingDto
import com.openclassrooms.realestatemanager.utils.GEOCODING_PATH
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingAPI {
    @GET(GEOCODING_PATH)
    suspend fun getGeoCoding(
        @Query("address") address: String,
        @Query("key") apiKey: String = API_KEY
    ): GeoCodingDto
}