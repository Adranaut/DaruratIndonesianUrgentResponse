package com.daruratindonesianurgentresponse.data.response

import com.google.gson.annotations.SerializedName

data class NearbyResponse(

	@field:SerializedName("context")
	val context: Context? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null
)

data class Main(

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("longitude")
	val longitude: Any? = null
)

data class CategoriesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("icon")
	val icon: Icon? = null,

	@field:SerializedName("short_name")
	val shortName: String? = null,

	@field:SerializedName("plural_name")
	val pluralName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class Roof(

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("longitude")
	val longitude: Any? = null
)

data class Parent(

	@field:SerializedName("fsq_id")
	val fsqId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("categories")
	val categories: List<CategoriesItem?>? = null
)

data class RelatedPlaces(

	@field:SerializedName("parent")
	val parent: Parent? = null,

	@field:SerializedName("children")
	val children: List<ChildrenItem?>? = null
)

data class Location(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("formatted_address")
	val formattedAddress: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("locality")
	val locality: String? = null,

	@field:SerializedName("postcode")
	val postcode: String? = null,

	@field:SerializedName("region")
	val region: String? = null,

	@field:SerializedName("cross_street")
	val crossStreet: String? = null
)

data class DropOff(

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("longitude")
	val longitude: Any? = null
)

data class Geocodes(

	@field:SerializedName("roof")
	val roof: Roof? = null,

	@field:SerializedName("drop_off")
	val dropOff: DropOff? = null,

	@field:SerializedName("main")
	val main: Main? = null
)

data class Center(

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("longitude")
	val longitude: Any? = null
)

data class Context(

	@field:SerializedName("geo_bounds")
	val geoBounds: GeoBounds? = null
)

data class Circle(

	@field:SerializedName("center")
	val center: Center? = null,

	@field:SerializedName("radius")
	val radius: Int? = null
)

data class ResultsItem(

	@field:SerializedName("fsq_id")
	val fsqId: String? = null,

	@field:SerializedName("distance")
	val distance: Int? = null,

	@field:SerializedName("chains")
	val chains: List<Any?>? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("closed_bucket")
	val closedBucket: String? = null,

	@field:SerializedName("related_places")
	val relatedPlaces: RelatedPlaces? = null,

	@field:SerializedName("geocodes")
	val geocodes: Geocodes? = null,

	@field:SerializedName("location")
	val location: Location? = null,

	@field:SerializedName("categories")
	val categories: List<CategoriesItem?>? = null
)

data class Icon(

	@field:SerializedName("prefix")
	val prefix: String? = null,

	@field:SerializedName("suffix")
	val suffix: String? = null
)

data class ChildrenItem(

	@field:SerializedName("fsq_id")
	val fsqId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("categories")
	val categories: List<CategoriesItem?>? = null
)

data class GeoBounds(

	@field:SerializedName("circle")
	val circle: Circle? = null
)
