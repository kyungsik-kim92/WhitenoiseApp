package com.example.whitenoiseapp.domain.usecase

import com.example.whitenoiseapp.domain.repository.MediaPlayerRepository
import com.example.whitenoiseapp.domain.repository.PlayRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TogglePlaySelectionUseCase @Inject constructor(
    private val playRepository: PlayRepository,
    private val mediaPlayerRepository: MediaPlayerRepository
) {
    
    suspend operator fun invoke(index: Int) {
        try {
            val currentList = playRepository.getPlayList().first()
            val currentSelection = currentList[index].isSelected
            
            playRepository.togglePlaySelection(index)

            if (currentSelection) {
                mediaPlayerRepository.stopMediaPlayer(index)
            } else {
                mediaPlayerRepository.startMediaPlayer(index)
            }
            
        } catch (e: Exception) {
            throw e
        }
    }
}