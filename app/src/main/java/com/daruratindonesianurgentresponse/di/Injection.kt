package com.daruratindonesianurgentresponse.di

import android.content.Context
import com.daruratindonesianurgentresponse.data.Repository
import com.daruratindonesianurgentresponse.data.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}