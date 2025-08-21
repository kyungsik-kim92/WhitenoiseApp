package com.example.domain

import com.example.whitenoiseapp.domain.model.TimerModel
import com.example.whitenoiseapp.domain.repository.TimerRepository
import com.example.whitenoiseapp.domain.usecase.GetTimerListUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class GetTimerListUseCaseTest {

    @Mock
    private lateinit var timerRepository: TimerRepository

    private lateinit var getTimerListUseCase: GetTimerListUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getTimerListUseCase = GetTimerListUseCase(timerRepository)
    }

    @Test
    fun `invoke should return timer list from repository`() = runTest {

        val timerList = listOf(
            TimerModel(ms = 0L, timerStr = "끄기", isSelected = false),
            TimerModel(ms = 300000L, timerStr = "5분", isSelected = true),
            TimerModel(ms = 600000L, timerStr = "10분", isSelected = false),
            TimerModel(ms = 1800000L, timerStr = "30분", isSelected = false)
        )

        whenever(timerRepository.getTimerList()).thenReturn(flowOf(timerList))


        val result = getTimerListUseCase().single()


        assertEquals(timerList, result)
        assertEquals(4, result.size)
        assertEquals("끄기", result[0].timerStr)
        assertEquals(true, result[1].isSelected)
        assertEquals(600000L, result[2].ms)
    }

    @Test
    fun `invoke should return empty list when repository returns empty`() = runTest {

        val emptyList = emptyList<TimerModel>()
        whenever(timerRepository.getTimerList()).thenReturn(flowOf(emptyList))


        val result = getTimerListUseCase().single()

        assertEquals(emptyList, result)
        assertEquals(0, result.size)
    }
}