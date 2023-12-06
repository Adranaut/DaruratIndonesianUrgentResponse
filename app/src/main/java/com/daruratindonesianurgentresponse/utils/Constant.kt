package com.daruratindonesianurgentresponse.utils

import com.google.android.gms.maps.model.LatLng

lateinit var ADDRESS: String
lateinit var TYPE: String
lateinit var LOCATION: LatLng

const val POLICE = "police"
const val MEDICINE = "hospital"
const val FIREFIGHTER = "fire_station"

var STATUS: Boolean = false
var STATUSGPS: Boolean = false
var STATUSMAP: Boolean = false
var LATITUDE: Double = 0.0
var LONGITUDE: Double = 0.0
var BOTTOMNAV: Int = 1
