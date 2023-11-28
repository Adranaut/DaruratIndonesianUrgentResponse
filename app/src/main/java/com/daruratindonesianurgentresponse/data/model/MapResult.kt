package com.daruratindonesianurgentresponse.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MapResult : Serializable {
    @SerializedName("modelGeometry")
    val modelGeometry: MapGeometry? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("vicinity")
    val vicinity: String? = null

    @SerializedName("placeId")
    val placeId: String? = null
}
