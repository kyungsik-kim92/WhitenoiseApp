package com.example.domain

import com.example.whitenoiseapp.domain.model.TimerModel
import com.example.whitenoiseapp.domain.repository.MediaPlayerRepository
import com.example.whitenoiseapp.domain.repository.TimerRepository
import com.example.whitenoiseapp.domain.usecase.SelectTimerUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SelectTimerUseCaseTest {
    @Mock
    private lateinit var timerRepository: TimerRepository

    @Mock
    private lateinit var mediaPlayerRepository: MediaPlayerRepository

    private lateinit var selectTimerUseCase: SelectTimerUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        selectTimerUseCase = SelectTimerUseCase(timerRepository, mediaPlayerRepository)
    }

    @Test
    fun `invoke should select timer and setup when music is playing`() = runTest {
        val index = 1
        val selectedTimer = TimerModel(
            ms = 300000L,
            timerStr = "5분",
            isSelected = true
        )

        whenever(mediaPlayerRepository.isPlaying()).thenReturn(true)
        whenever(timerRepository.getSelectedTimer()).thenReturn(flowOf(selectedTimer))

        val result = selectTimerUseCase(index)

        assertTrue(result.isSuccess)
        assertEquals("타이머 설정: 5분", result.getOrNull())
        verify(timerRepository).selectTimer(index)
        verify(mediaPlayerRepository).setupTimer(300000L)
    }

    @Test
    fun `invoke should return failure when music is not playing`() = runTest {
        val index = 1
        whenever(mediaPlayerRepository.isPlaying()).thenReturn(false)

        val result = selectTimerUseCase(index)

        assertTrue(result.isFailure)
        assertEquals("음악을 선택해 주세요", result.exceptionOrNull()?.message)
    }

}