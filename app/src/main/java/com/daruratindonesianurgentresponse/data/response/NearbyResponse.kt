package com.daruratindonesianurgentresponse.data.response

import com.google.gson.annotations.SerializedName

data class NearbyResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("website")
	val website: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("distance")
	val distance: Any? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("telepon")
	val telepon: String? = null,

	@field:SerializedName("available")
	val available: Int? = null,

	@field:SerializedName("wa")
	val wa: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)
