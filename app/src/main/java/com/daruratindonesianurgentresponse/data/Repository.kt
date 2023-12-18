package com.daruratindonesianurgentresponse.data

import androidx.lifecycle.LiveData
import com.daruratindonesianurgentresponse.data.api.ApiService
import com.daruratindonesianurgentresponse.data.api.ApiServiceChat
import com.daruratindonesianurgentresponse.data.local.MessageDao
import com.daruratindonesianurgentresponse.data.local.MessageDatabase
import com.daruratindonesianurgentresponse.data.local.MessageEntity
import com.daruratindonesianurgentresponse.data.response.ChatResponse
import com.daruratindonesianurgentresponse.data.response.MessageRequestBody
import com.daruratindonesianurgentresponse.data.response.NearbyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class Repository private constructor(
    private val apiService: ApiService,
    private val messageDao: MessageDao,
    private val apiServiceChat: ApiServiceChat
) {

    val allMessages: LiveData<List<MessageEntity>> = messageDao.getAllMessages()

    suspend fun sendMessage(message: MessageEntity) {
        withContext(Dispatchers.IO) {
            messageDao.insertMessage(message)
        }
    }

    suspend fun getNearbyPlaces(type: String, lat: String, lon: String, radius: String): Flow<Result<NearbyResponse>> = flow {
        try {
            val response = apiService.getNearbyPlaces(type, lat, lon, radius)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun inputChatBot(messageRequestBody: MessageRequestBody): Flow<Result<ChatResponse>> = flow {
        try {
            val response = apiServiceChat.inputChatBot(messageRequestBody)
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
            apiService: ApiService,
            database: MessageDatabase,
            apiServiceChat: ApiServiceChat
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, database.messageDao(), apiServiceChat)
            }.also { instance = it }
    }
}