package com.daruratindonesianurgentresponse.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MapLocation : Serializable {
    @SerializedName("lat")
    val lat: Double? = null

    @SerializedName("lng")
    val lng: Double? = null
}
