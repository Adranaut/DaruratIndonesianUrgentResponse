package com.daruratindonesianurgentresponse.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages")
    fun getAllMessages(): LiveData<List<MessageEntity>>

    @Insert
    fun insertMessage(message: MessageEntity)
}