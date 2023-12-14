package com.daruratindonesianurgentresponse.utils

import com.google.android.gms.maps.model.LatLng

lateinit var ADDRESS: String
lateinit var TYPE: String
lateinit var LOCATION: LatLng

const val TYPEPOLICE = "polisi"
const val TYPEAMBULANCE = "ambulan"
const val TYPEFIREFIGHTER = "pemadam"
const val TYPEMEDICE = "faskes"
const val PRIMARYRADIUS = "10000"
const val SECONDARYRYRADIUS = "30000"

var STATUS: Boolean = false
var STATUSGPS: Boolean = false
var STATUSMAP: Boolean = false
var LATITUDE: Double = 0.0
var LONGITUDE: Double = 0.0
var BOTTOMNAV: Int = 1
var SPINNERINDEX: Int = 0
