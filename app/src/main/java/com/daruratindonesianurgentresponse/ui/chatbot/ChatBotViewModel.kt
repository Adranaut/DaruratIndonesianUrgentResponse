package com.daruratindonesianurgentresponse.ui.chatbot

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.daruratindonesianurgentresponse.data.Repository
import com.daruratindonesianurgentresponse.data.local.MessageEntity
import com.daruratindonesianurgentresponse.data.response.ChatResponse
import com.daruratindonesianurgentresponse.data.response.MessageRequestBody
import kotlinx.coroutines.flow.Flow

class ChatBotViewModel(private val repository: Repository) : ViewModel() {

    val allMessages: LiveData<List<MessageEntity>> = repository.allMessages

    suspend fun sendMessage(message: MessageEntity) {
        repository.sendMessage(message)
    }

    suspend fun inputChatBot(message: String): Flow<Result<ChatResponse>> {
        val requestBody = MessageRequestBody(message)
        return repository.inputChatBot(requestBody)
    }

}