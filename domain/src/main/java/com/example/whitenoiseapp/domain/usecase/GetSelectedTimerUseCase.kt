package com.example.whitenoiseapp.domain.usecase

import com.example.whitenoiseapp.domain.model.TimerModel
import com.example.whitenoiseapp.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSelectedTimerUseCase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(): Flow<TimerModel?> {
        return timerRepository.getSelectedTimer()
    }
}
