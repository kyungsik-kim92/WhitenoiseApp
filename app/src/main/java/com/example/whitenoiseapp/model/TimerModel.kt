package com.example.whitenoiseapp.model

data class TimerModel(
    val timerStr: String,
    val ms: Long = 0,
    var isSelected: Boolean = false
)