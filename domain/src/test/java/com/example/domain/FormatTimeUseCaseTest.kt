package com.example.domain

import com.example.whitenoiseapp.domain.repository.MediaPlayerRepository
import com.example.whitenoiseapp.domain.usecase.FormatTimeUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class FormatTimeUseCaseTest {
    @Mock
    private lateinit var mediaPlayerRepository: MediaPlayerRepository

    private lateinit var formatTimeUseCase: FormatTimeUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        formatTimeUseCase = FormatTimeUseCase(mediaPlayerRepository)
    }

    @Test
    fun `invoke should return formatted time from repository`() {
        val ms = 60000L // 1분
        val prefix = "TIMER"
        val expected = "TIMER 01:00"

        whenever(mediaPlayerRepository.formatTime(ms, prefix))
            .thenReturn(expected)


        val result = formatTimeUseCase(ms, prefix)

        assertEquals(expected, result)
    }

}