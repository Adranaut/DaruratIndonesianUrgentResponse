package com.daruratindonesianurgentresponse.data.response

import com.daruratindonesianurgentresponse.data.model.MapResult
import com.google.gson.annotations.SerializedName

class NearbyResult {

    @SerializedName("results")
    val mapResults: List<MapResult> = emptyList()
}