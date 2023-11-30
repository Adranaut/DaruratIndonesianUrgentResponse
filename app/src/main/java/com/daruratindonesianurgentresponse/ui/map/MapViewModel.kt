package com.daruratindonesianurgentresponse.ui.map

import androidx.lifecycle.ViewModel
import com.daruratindonesianurgentresponse.data.Repository
import com.daruratindonesianurgentresponse.data.response.NearbyResponse
import kotlinx.coroutines.flow.Flow

class MapViewModel(private val repository: Repository) : ViewModel() {

    suspend fun getNearbyPlaces(ll: String, radius: String, categories: String): Flow<Result<NearbyResponse>> =
        repository.getNearbyPlaces(ll, radius, categories)

}