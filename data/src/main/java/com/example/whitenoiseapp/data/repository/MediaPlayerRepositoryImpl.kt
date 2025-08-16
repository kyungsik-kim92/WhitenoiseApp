package com.example.whitenoiseapp.data.repository

import com.example.whitenoiseapp.core.utils.TimeFormatter
import com.example.whitenoiseapp.data.datasource.service.MediaPlayerDataSource
import com.example.whitenoiseapp.domain.model.TimerState
import com.example.whitenoiseapp.domain.repository.MediaPlayerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaPlayerRepositoryImpl @Inject constructor(
    private val mediaPlayerDataSource: MediaPlayerDataSource
) : MediaPlayerRepository {

    override suspend fun startMediaPlayer(index: Int) {
        mediaPlayerDataSource.startMediaPlayer(index)
    }

    override suspend fun stopMediaPlayer(index: Int) {
        mediaPlayerDataSource.stopMediaPlayer(index)
    }

    override suspend fun isPlaying(): Boolean {
        return mediaPlayerDataSource.isPlaying()
    }

    override suspend fun setupTimer(timeMs: Long) {
        mediaPlayerDataSource.setupTimer(timeMs)
    }

    override fun getTimerState(): Flow<TimerState> {
        return mediaPlayerDataSource.getTimerState()
    }

    override fun getRemainingTime(): Long {
        return mediaPlayerDataSource.getRemainingTime()
    }

    override fun formatTime(ms: Long, prefix: String): String {
        return TimeFormatter.formatTimeWithPrefix(ms, prefix)
    }
}
