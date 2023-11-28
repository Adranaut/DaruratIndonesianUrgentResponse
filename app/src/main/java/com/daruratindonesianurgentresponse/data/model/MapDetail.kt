package com.daruratindonesianurgentresponse.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MapDetail : Serializable {
    @SerializedName("modelGeometry")
    val modelGeometry: MapGeometry? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("address")
    val address: String? = null

    @SerializedName("phoneNumber")
    val phoneNumber: String? = null
}
