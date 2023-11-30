package com.daruratindonesianurgentresponse.data.api

import com.daruratindonesianurgentresponse.data.response.NearbyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search")
    suspend fun getNearbyPlaces(
        @Query("ll") key: String,
        @Query("radius") keyword: String,
        @Query("categories") location: String,
    ): NearbyResponse
}