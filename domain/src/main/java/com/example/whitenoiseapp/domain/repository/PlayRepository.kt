package com.example.whitenoiseapp.domain.repository

import com.example.whitenoiseapp.domain.model.PlayModel
import kotlinx.coroutines.flow.Flow

interface PlayRepository {
    fun getPlayList(): Flow<List<PlayModel>>
    suspend fun togglePlaySelection(index: Int)
    suspend fun clearAllSelections()
}
