package com.daruratindonesianurgentresponse.di

import android.content.Context
import com.daruratindonesianurgentresponse.BuildConfig
import com.daruratindonesianurgentresponse.data.Repository
import com.daruratindonesianurgentresponse.data.api.ApiConfig
import com.daruratindonesianurgentresponse.data.local.MessageDatabase

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService(BuildConfig.PLACE_KEY)
        val database = MessageDatabase.getInstance(context)
        return Repository.getInstance(apiService, database)
    }
}