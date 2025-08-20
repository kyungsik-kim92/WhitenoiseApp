package com.example.domain

import com.example.whitenoiseapp.domain.model.TimerModel
import com.example.whitenoiseapp.domain.repository.TimerRepository
import com.example.whitenoiseapp.domain.usecase.GetSelectedTimerUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class GetSelectedTimerUseCaseTest {
    @Mock
    private lateinit var timerRepository: TimerRepository

    private lateinit var getSelectedTimerUseCase: GetSelectedTimerUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getSelectedTimerUseCase = GetSelectedTimerUseCase(timerRepository)
    }

    @Test
    fun `invoke should return selected timer from repository`() = runTest {
        val selectedTimer = TimerModel(
            ms = 300000L,
            timerStr = "5분",
            isSelected = true
        )

        whenever(timerRepository.getSelectedTimer()).thenReturn(flowOf(selectedTimer))

        val result = getSelectedTimerUseCase().single()

        assertEquals(selectedTimer, result)
        assertEquals(300000L, result?.ms)
        assertEquals("5분", result?.timerStr)
        assertEquals(true, result?.isSelected)
    }


}