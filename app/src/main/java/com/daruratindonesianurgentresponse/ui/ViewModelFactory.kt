package com.daruratindonesianurgentresponse.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daruratindonesianurgentresponse.data.Repository
import com.daruratindonesianurgentresponse.di.Injection
import com.daruratindonesianurgentresponse.ui.chatbot.ChatBotViewModel
import com.daruratindonesianurgentresponse.ui.map.MapViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {

    //Untuk Instance ViewModel Disini
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                MapViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ChatBotViewModel::class.java) -> {
                ChatBotViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    //Untuk Instance Repository dan Injection Disini
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}