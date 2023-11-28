package com.daruratindonesianurgentresponse.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MapGeometry : Serializable {
    @SerializedName("modelLocation")
    val modelLocation: MapLocation? = null
}
