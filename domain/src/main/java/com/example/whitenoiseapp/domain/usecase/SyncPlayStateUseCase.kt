package com.example.whitenoiseapp.domain.usecase

import com.example.whitenoiseapp.domain.model.PlayModel
import com.example.whitenoiseapp.domain.repository.MediaPlayerRepository
import com.example.whitenoiseapp.domain.repository.PlayRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SyncPlayStateUseCase  @Inject constructor(
    private val playRepository: PlayRepository,
    private val mediaPlayerRepository: MediaPlayerRepository
) {
    suspend operator fun invoke(): List<PlayModel> {
        try {
            val playingIndexes = mediaPlayerRepository.getPlayingIndex()
            val currentList = playRepository.getPlayList().first()

            currentList.forEachIndexed { index, play ->
                val shouldBeSelected = index in playingIndexes
                if (play.isSelected != shouldBeSelected) {
                    playRepository.togglePlaySelection(index)
                }
            }

            return playRepository.getPlayList().first()
        } catch (e: Exception) {
            return playRepository.getPlayList().first()
        }
    }
}