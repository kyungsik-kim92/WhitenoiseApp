package com.example.whitenoiseapp.model

data class TimerModel(
    val timerStr: String,
    val ms: Long = 0,
    val isSelected: Boolean = false
)