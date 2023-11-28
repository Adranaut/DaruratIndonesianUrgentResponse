package com.daruratindonesianurgentresponse.data

import com.daruratindonesianurgentresponse.data.api.ApiService

class Repository private constructor(
    private val apiService: ApiService,
) {

//    suspend fun getDetail(token: String, id: String): Flow<Result<DetailResponse>> = flow {
//        try {
//            val response = ApiConfig.getApiService(token).getDetail(id)
//            emit(Result.success(response))
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emit(Result.failure(e))
//        }
//    }

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