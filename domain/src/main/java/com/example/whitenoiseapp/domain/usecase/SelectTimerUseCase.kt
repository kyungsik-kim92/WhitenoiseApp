package com.example.whitenoiseapp.domain.usecase

import com.example.whitenoiseapp.domain.repository.MediaPlayerRepository
import com.example.whitenoiseapp.domain.repository.TimerRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SelectTimerUseCase @Inject constructor(
    private val timerRepository: TimerRepository,
    private val mediaPlayerRepository: MediaPlayerRepository
) {
    suspend operator fun invoke(index: Int): Result<String> {
        return try {
            if (!mediaPlayerRepository.isPlaying()) {
                return Result.failure(Exception("음악을 선택해 주세요"))
            }

            timerRepository.selectTimer(index)

            val selectedTimer = timerRepository.getSelectedTimer().first()
            selectedTimer?.let { timer ->
                val timeToSet = if (timer.ms > 0L) timer.ms else 0L
                mediaPlayerRepository.setupTimer(timeToSet)

                val message = if (timer.ms > 0L) {
                    "타이머 설정: ${timer.timerStr}"
                } else {
                    "타이머 해제"
                }
                Result.success(message)
            } ?: Result.failure(Exception("타이머 설정 실패"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
