package com.daruratindonesianurgentresponse.ui.chatbot

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.daruratindonesianurgentresponse.data.Repository
import com.daruratindonesianurgentresponse.data.local.MessageEntity

class ChatBotViewModel(private val repository: Repository) : ViewModel() {

    val allMessages: LiveData<List<MessageEntity>> = repository.allMessages

    suspend fun sendMessage(message: MessageEntity) {
        repository.sendMessage(message)
    }

}