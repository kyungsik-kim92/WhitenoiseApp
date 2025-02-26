package com.example.whitenoiseapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitenoiseapp.constants.Constants
import com.example.whitenoiseapp.model.TimerModel
import com.example.whitenoiseapp.service.WhiteNoiseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _isServiceReady = MutableStateFlow(false)
    val isServiceReady = _isServiceReady.asStateFlow()

    fun setServiceReady(ready: Boolean) {
        _isServiceReady.value = ready
    }

    private val _timerFinishedEvent = MutableSharedFlow<Unit>(replay = 0)
    val timerFinishedEvent = _timerFinishedEvent.asSharedFlow()

    private val _scheduledTime = MutableStateFlow<List<TimerModel>>(emptyList())
    val scheduledTime = _scheduledTime.asStateFlow()

    private val _realTime = MutableStateFlow(0L)
    val realTime = _realTime.asStateFlow()

    init {
        _scheduledTime.value = Constants.getTimerList()
    }

    fun observeTimerState(timerState: Flow<WhiteNoiseService.TimerState>) {
        viewModelScope.launch {
            timerState.collect { state ->
                when (state) {
                    is WhiteNoiseService.TimerState.Update -> {
                        _realTime.value = state.ms
                    }

                    is WhiteNoiseService.TimerState.Finish -> {
                        _scheduledTime.update { currentList ->
                            currentList.map { timer ->
                                timer.apply {
                                    setIsSelected(false)
                                }
                            }
                        }
                        _timerFinishedEvent.emit(Unit)
                    }
                }
            }
        }
    }
}