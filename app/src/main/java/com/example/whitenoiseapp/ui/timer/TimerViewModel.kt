package com.example.whitenoiseapp.ui.timer

import androidx.lifecycle.ViewModel
import com.example.whitenoiseapp.constants.Constants
import com.example.whitenoiseapp.model.TimerModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerViewModel : ViewModel() {
    private val _timerList = MutableStateFlow<List<TimerModel>>(emptyList())
    val timerList = _timerList.asStateFlow()

    private var _playTime = MutableStateFlow(0L)
    val playTime = _playTime.asStateFlow()

    private var _isTimerSet = MutableStateFlow(false)
    val isTimerSet = _isTimerSet.asStateFlow()

    init {
        _timerList.value = Constants.getTimerList()
    }


    fun setPlayTime(ms: Long) {
        _playTime.value = ms
    }

    fun setIsTimerSet(isTimerSet: Boolean) {
        _isTimerSet.value = isTimerSet
    }

}