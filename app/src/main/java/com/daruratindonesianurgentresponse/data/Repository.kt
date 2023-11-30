package com.daruratindonesianurgentresponse.data

import com.daruratindonesianurgentresponse.data.api.ApiService
import com.daruratindonesianurgentresponse.data.response.NearbyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository private constructor(
    private val apiService: ApiService,
) {

    suspend fun getNearbyPlaces(ll: String, radius: String, categories: String): Flow<Result<NearbyResponse>> = flow {
        try {
            val response = apiService.getNearbyPlaces(ll, radius, categories)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    //Instance Repository
    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}