package com.example.whitenoiseapp.domain.usecase

import com.example.whitenoiseapp.domain.repository.MediaPlayerRepository
import javax.inject.Inject

class FormatTimeUseCase @Inject constructor(
    private val mediaPlayerRepository: MediaPlayerRepository
) {
    operator fun invoke(ms: Long, prefix: String): String {
        return mediaPlayerRepository.formatTime(ms, prefix)
    }
}
