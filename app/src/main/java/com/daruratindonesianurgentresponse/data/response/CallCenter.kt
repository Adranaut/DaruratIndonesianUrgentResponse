package com.daruratindonesianurgentresponse.data.response

import com.google.gson.annotations.SerializedName

data class CallCenter(

    @field:SerializedName("service_name")
    val serviceName: String? = null,

    @field:SerializedName("service_number")
    val serviceNumber: String? = null
)
