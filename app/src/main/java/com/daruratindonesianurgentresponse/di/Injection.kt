package com.daruratindonesianurgentresponse.di

import android.content.Context
import com.daruratindonesianurgentresponse.BuildConfig
import com.daruratindonesianurgentresponse.data.Repository
import com.daruratindonesianurgentresponse.data.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService(BuildConfig.FOURSQUARE_KEY)
        return Repository.getInstance(apiService)
    }
}