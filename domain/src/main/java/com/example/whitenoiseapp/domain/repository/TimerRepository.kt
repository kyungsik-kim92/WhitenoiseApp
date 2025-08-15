package com.example.whitenoiseapp.domain.repository

import com.example.whitenoiseapp.domain.model.TimerModel
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    fun getTimerList(): Flow<List<TimerModel>>
    suspend fun selectTimer(index: Int)
    fun getSelectedTimer(): Flow<TimerModel?>
}
