package com.example.whitenoiseapp.data.repository

import com.example.whitenoiseapp.data.datasource.local.TimerLocalDataSource
import com.example.whitenoiseapp.domain.model.TimerModel
import com.example.whitenoiseapp.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(
    private val timerLocalDataSource: TimerLocalDataSource
) : TimerRepository {

    override fun getTimerList(): Flow<List<TimerModel>> {
        return timerLocalDataSource.getTimerList()
    }

    override suspend fun selectTimer(index: Int) {
        timerLocalDataSource.selectTimer(index)
    }

    override fun getSelectedTimer(): Flow<TimerModel?> {
        return timerLocalDataSource.getSelectedTimer()
    }
}
