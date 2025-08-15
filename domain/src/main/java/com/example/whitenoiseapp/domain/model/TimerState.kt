package com.example.whitenoiseapp.domain.model

sealed class TimerState {
    data class Update(val ms: Long) : TimerState()
    data class Paused(val ms: Long) : TimerState()
    data class Resumed(val ms: Long) : TimerState()
    data object Finish : TimerState()
}

