package com.example.whitenoiseapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitenoiseapp.constants.Constants
import com.example.whitenoiseapp.model.TimerModel
import com.example.whitenoiseapp.service.WhiteNoiseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _scheduledTime = MutableStateFlow<List<TimerModel>>(emptyList())
    val scheduledTime = _scheduledTime.asStateFlow()

    private val _realTime = MutableStateFlow(0L)
    val realTime = _realTime.asStateFlow()

    private var _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    init {
        _scheduledTime.value = Constants.getTimerList()
    }

    fun observeTimerState(timerState: Flow<WhiteNoiseService.TimerState>) {
        viewModelScope.launch {
            timerState.collect { state ->
                when (state) {
                    is WhiteNoiseService.TimerState.Update -> {
                        _realTime.value = state.ms
                        _isPlaying.value = true
                    }

                    is WhiteNoiseService.TimerState.Finish -> {
                        _isPlaying.value = false
                    }
                }
            }
        }
    }
}