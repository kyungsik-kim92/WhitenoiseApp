package com.example.whitenoiseapp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerModel(
    var timerStr: String,
    var ms: Long = 0,
    private val _isSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
) {
    val isSelected: StateFlow<Boolean> = _isSelected.asStateFlow()

    fun setIsSelected(selected: Boolean) {
        _isSelected.value = selected
    }
}