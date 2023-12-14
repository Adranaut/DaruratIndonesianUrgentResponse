package com.daruratindonesianurgentresponse.ui.map

import androidx.lifecycle.ViewModel
import com.daruratindonesianurgentresponse.data.Repository
import com.daruratindonesianurgentresponse.data.response.NearbyResponse
import kotlinx.coroutines.flow.Flow

class MapViewModel(private val repository: Repository) : ViewModel() {

    suspend fun getNearbyPlaces(type: String, lat: String, lon: String, radius: String): Flow<Result<NearbyResponse>> =
        repository.getNearbyPlaces(type, lat, lon, radius)

}