package com.example.domain

import com.example.whitenoiseapp.domain.model.TimerState
import com.example.whitenoiseapp.domain.repository.MediaPlayerRepository
import com.example.whitenoiseapp.domain.usecase.ObserveTimerStateUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class ObserveTimerStateUseCaseTest {
    @Mock
    private lateinit var mediaPlayerRepository: MediaPlayerRepository

    private lateinit var observeTimerStateUseCase: ObserveTimerStateUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        observeTimerStateUseCase = ObserveTimerStateUseCase(mediaPlayerRepository)
    }

    @Test
    fun `invoke should return timer update state`() = runTest {

        val timerState = TimerState.Update(120000L)
        whenever(mediaPlayerRepository.getTimerState()).thenReturn(flowOf(timerState))

        val result = observeTimerStateUseCase().single()

        assertEquals(timerState, result)
        assertEquals(120000L, (result as TimerState.Update).ms)
    }

    @Test
    fun `invoke should return timer paused state`() = runTest {
        val timerState = TimerState.Paused(60000L)
        whenever(mediaPlayerRepository.getTimerState()).thenReturn(flowOf(timerState))

        val result = observeTimerStateUseCase().single()

        assertEquals(timerState, result)
        assertEquals(60000L, (result as TimerState.Paused).ms)
    }

    @Test
    fun `invoke should return timer resumed state`() = runTest {
        val timerState = TimerState.Resumed(90000L)
        whenever(mediaPlayerRepository.getTimerState()).thenReturn(flowOf(timerState))


        val result = observeTimerStateUseCase().single()

        assertEquals(timerState, result)
        assertEquals(90000L, (result as TimerState.Resumed).ms)
    }

    @Test
    fun `invoke should return timer finish state`() = runTest {
        val timerState = TimerState.Finish
        whenever(mediaPlayerRepository.getTimerState()).thenReturn(flowOf(timerState))


        val result = observeTimerStateUseCase().single()

        assertEquals(timerState, result)
    }

}