package com.example.domain

import com.example.whitenoiseapp.domain.model.PlayModel
import com.example.whitenoiseapp.domain.repository.MediaPlayerRepository
import com.example.whitenoiseapp.domain.repository.PlayRepository
import com.example.whitenoiseapp.domain.usecase.TogglePlaySelectionUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TogglePlaySelectionUseCaseTest {
    @Mock
    private lateinit var playRepository: PlayRepository

    @Mock
    private lateinit var mediaPlayerRepository: MediaPlayerRepository

    private lateinit var togglePlaySelectionUseCase: TogglePlaySelectionUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        togglePlaySelectionUseCase =
            TogglePlaySelectionUseCase(playRepository, mediaPlayerRepository)
    }

    @Test
    fun `invoke should start media player when item is not selected`() = runTest {
        val index = 0
        val playList = listOf(
            PlayModel(
                iconResId = 1,
                musicResId = 1,
                isSelected = false
            )
        )

        whenever(playRepository.getPlayList()).thenReturn(flowOf(playList))

        togglePlaySelectionUseCase(index)

        verify(playRepository).togglePlaySelection(index)
        verify(mediaPlayerRepository).startMediaPlayer(index)
    }

    @Test
    fun `invoke should stop media player when item is already selected`() = runTest {
        val index = 1
        val playList = listOf(
            PlayModel(
                musicResId = 1,
                iconResId = 1,
                isSelected = false
            ),
            PlayModel(
                musicResId = 2,
                iconResId = 2,
                isSelected = true
            )
        )

        whenever(playRepository.getPlayList()).thenReturn(flowOf(playList))

        togglePlaySelectionUseCase(index)

        verify(playRepository).togglePlaySelection(index)
        verify(mediaPlayerRepository).stopMediaPlayer(index)
    }

}