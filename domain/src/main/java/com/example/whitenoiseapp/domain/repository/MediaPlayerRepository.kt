package com.example.whitenoiseapp.domain.repository

import com.example.whitenoiseapp.domain.model.TimerState
import kotlinx.coroutines.flow.Flow

interface MediaPlayerRepository {
    suspend fun startMediaPlayer(index: Int)
    suspend fun stopMediaPlayer(index: Int)
    suspend fun isPlaying(): Boolean
    suspend fun setupTimer(timeMs: Long)
    fun getTimerState(): Flow<TimerState>
    fun getRemainingTime(): Long
    fun getPlayingIndex(): List<Int>
    fun formatTime(ms: Long, prefix: String): String
}
