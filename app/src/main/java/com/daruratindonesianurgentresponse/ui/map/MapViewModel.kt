package com.daruratindonesianurgentresponse.ui.map

import androidx.lifecycle.ViewModel
import com.daruratindonesianurgentresponse.data.Repository
import com.daruratindonesianurgentresponse.data.response.GoogleResponse
import kotlinx.coroutines.flow.Flow

class MapViewModel(private val repository: Repository) : ViewModel() {

    suspend fun getNearbyPlaces(lat: String, lng: String, type: String): Flow<Result<GoogleResponse>> =
        repository.getNearbyPlaces(lat, lng, type)

}