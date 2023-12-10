package com.daruratindonesianurgentresponse.data.api

import com.daruratindonesianurgentresponse.data.response.NearbyResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
//    @GET("places")
//    suspend fun getNearbyPlaces(
//        @Query("lat") latitude: String,
//        @Query("lng") longitude: String,
//        @Query("type") type: String,
//    ): GoogleResponse

    @GET("{type}")
    suspend fun getNearbyPlaces(
        @Path("type") type: String,
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): NearbyResponse
}