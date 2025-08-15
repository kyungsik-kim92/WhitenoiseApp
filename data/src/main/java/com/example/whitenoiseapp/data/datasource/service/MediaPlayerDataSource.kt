package com.example.whitenoiseapp.data.datasource.service

import com.example.whitenoiseapp.data.service.WhiteNoiseService
import com.example.whitenoiseapp.domain.model.TimerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlayerDataSource @Inject constructor() {

    private var whiteNoiseService: WhiteNoiseService? = null

    fun setService(service: WhiteNoiseService) {
        this.whiteNoiseService = service
    }

    fun startMediaPlayer(index: Int) {
        if (whiteNoiseService != null) {
            whiteNoiseService?.startMediaPlayer(index)
        } else {
            throw IllegalStateException("WhiteNoiseService is not initialized")
        }
    }

    fun stopMediaPlayer(index: Int) {
        if (whiteNoiseService != null) {
            whiteNoiseService?.stopMediaPlayer(index)
        } else {
            throw IllegalStateException("WhiteNoiseService is not initialized")
        }
    }

    fun isPlaying(): Boolean {
        return whiteNoiseService?.isPlaying() ?: false
    }

    fun setupTimer(timeMs: Long) {
        if (whiteNoiseService != null) {
            whiteNoiseService?.setupTimer(timeMs)
        }
    }

    fun getTimerState(): Flow<TimerState> {
        return whiteNoiseService?.timerState?.map { serviceState ->
            when (serviceState) {
                is TimerState.Update -> TimerState.Update(serviceState.ms)
                is TimerState.Paused -> TimerState.Paused(serviceState.ms)
                is TimerState.Resumed -> TimerState.Resumed(serviceState.ms)
                is TimerState.Finish -> TimerState.Finish
            }
        } ?: emptyFlow()
    }

    fun getRemainingTime(): Long {
        return whiteNoiseService?.getRemainingTime() ?: 0L
    }
}



