package com.example.whitenoiseapp.domain.usecase

import com.example.whitenoiseapp.domain.model.TimerState
import com.example.whitenoiseapp.domain.repository.MediaPlayerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTimerStateUseCase @Inject constructor(
    private val mediaPlayerRepository: MediaPlayerRepository
) {
    operator fun invoke(): Flow<TimerState> {
        return mediaPlayerRepository.getTimerState()
    }
}
