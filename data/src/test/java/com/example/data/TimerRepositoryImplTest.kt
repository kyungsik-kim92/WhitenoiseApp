package com.example.data

import com.example.whitenoiseapp.data.datasource.local.TimerLocalDataSource
import com.example.whitenoiseapp.data.repository.TimerRepositoryImpl
import com.example.whitenoiseapp.domain.model.TimerModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class TimerRepositoryImplTest {

    private lateinit var timerLocalDataSource: TimerLocalDataSource
    private lateinit var timerRepository: TimerRepositoryImpl

    @Before
    fun setup() {
        timerLocalDataSource = mockk()
        timerRepository = TimerRepositoryImpl(timerLocalDataSource)
    }

    @Test
    fun `getTimerList should return timer list from data source`() = runTest {

        val timerList = listOf(
            TimerModel(timerStr = "끄기", ms = 0L, isSelected = false),
            TimerModel(timerStr = "5분", ms = 300000L, isSelected = true)
        )

        every { timerLocalDataSource.getTimerList() } returns flowOf(timerList)


        val result = timerRepository.getTimerList().single()

        assertEquals(timerList, result)
        assertEquals(2, result.size)
    }

    @Test
    fun `selectTimer should call data source selectTimer with correct index`() = runTest {

        val index = 2
        every { timerLocalDataSource.selectTimer(any()) } returns Unit

        timerRepository.selectTimer(index)

        verify { timerLocalDataSource.selectTimer(index) }
    }

    @Test
    fun `getSelectedTimer should return selected timer from data source`() = runTest {
        val selectedTimer = TimerModel(
            timerStr = "10분",
            ms = 600000L,
            isSelected = true
        )

        every { timerLocalDataSource.getSelectedTimer() } returns flowOf(selectedTimer)

        val result = timerRepository.getSelectedTimer().single()

        assertEquals(selectedTimer, result)
    }
}