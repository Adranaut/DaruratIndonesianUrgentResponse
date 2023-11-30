package com.daruratindonesianurgentresponse.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

object CalculateDistance {

    //Rumus menghitung jarak
    fun getDistance(destinationLatitude: Double, destinationLongitude: Double): Double {
        val earthRadius = 3958.75
        val dLatitude = Math.toRadians(destinationLatitude - LATITUDE)
        val dLongitude = Math.toRadians(destinationLongitude - LONGITUDE)
        val a = sin(dLatitude / 2) * sin(dLatitude / 2) +
                cos(Math.toRadians(LATITUDE)) *
                cos(Math.toRadians(destinationLatitude)) *
                sin(dLongitude / 2) * sin(dLongitude / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val dDistance = earthRadius * c
        val meterConversion = 1609
        val myDistance = dDistance * meterConversion
        return floor(myDistance / 1000)
    }
}