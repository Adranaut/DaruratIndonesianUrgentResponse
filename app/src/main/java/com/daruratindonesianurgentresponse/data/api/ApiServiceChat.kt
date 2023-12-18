package com.daruratindonesianurgentresponse.data.api

import com.daruratindonesianurgentresponse.data.response.ChatResponse
import com.daruratindonesianurgentresponse.data.response.MessageRequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceChat {

    @POST("chat")
    suspend fun inputChatBot(
        @Body message: MessageRequestBody
    ): ChatResponse
}