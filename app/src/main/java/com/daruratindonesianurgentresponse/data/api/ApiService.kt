package com.daruratindonesianurgentresponse.data.api

import com.daruratindonesianurgentresponse.data.response.GoogleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("places")
    suspend fun getNearbyPlaces(
        @Query("lat") key: String,
        @Query("lng") keyword: String,
        @Query("type") location: String,
    ): GoogleResponse
}