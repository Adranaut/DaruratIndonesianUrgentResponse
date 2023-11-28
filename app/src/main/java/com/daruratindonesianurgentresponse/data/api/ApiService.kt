package com.daruratindonesianurgentresponse.data.api

import com.daruratindonesianurgentresponse.data.response.DetailResult
import com.daruratindonesianurgentresponse.data.response.NearbyResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("nearbysearch/json")
    fun getNearbyResult(
        @Query("key") key: String,
        @Query("keyword") keyword: String,
        @Query("location") location: String,
        @Query("rankby") rankby: String?)
    : Call<NearbyResult>

    @GET("details/json")
    fun getDetailResult(
        @Query("key") key: String,
        @Query("placeid") placeid: String
    ): Call<DetailResult>
}