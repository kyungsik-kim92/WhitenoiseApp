package com.example.whitenoiseapp.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitenoiseapp.constants.Constants
import com.example.whitenoiseapp.model.TimerModel
import com.example.whitenoiseapp.service.WhiteNoiseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
//    private val _timerList = MutableStateFlow<List<TimerModel>>(emptyList())
//    val timerList = _timerList.asStateFlow()

    private val _playTime = MutableStateFlow(0L)
    val playTime = _playTime.asStateFlow()

    private val _timerState =
        MutableStateFlow<WhiteNoiseService.TimerState>(WhiteNoiseService.TimerState.Update(0))
    val timerState = _timerState.asStateFlow()

    fun updateTimerState(state: WhiteNoiseService.TimerState) {
        _timerState.value = state
    }

}