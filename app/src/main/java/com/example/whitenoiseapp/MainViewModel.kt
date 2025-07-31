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
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MainUiEvent>()
    val events = _events.asSharedFlow()

    private val _timerList = MutableStateFlow<List<TimerModel>>(emptyList())
    val timerList = _timerList.asStateFlow()

    private val _realTime = MutableStateFlow(0L)
    val realTime = _realTime.asStateFlow()

    init {
        _timerList.value = Constants.getTimerList()
        _uiState.value = MainUiState.Success()
    }

    fun setServiceReady(ready: Boolean) {
        val currentState = _uiState.value
        if (currentState is MainUiState.Success) {
            _uiState.value = currentState.copy(isServiceReady = ready)
        }

        if (ready) {
            viewModelScope.launch {
                _events.emit(MainUiEvent.ServiceReady)
            }
        }
    }

    fun observeTimerState(timerState: Flow<WhiteNoiseService.TimerState>) {
        viewModelScope.launch {
            timerState.collect { state ->
                when (state) {
                    is WhiteNoiseService.TimerState.Update -> {
                        _realTime.value = state.ms
                        val currentState = _uiState.value
                        if (currentState is MainUiState.Success) {
                            _uiState.value = currentState.copy(currentRealTime = state.ms)
                        }
                    }

                    is WhiteNoiseService.TimerState.Finish -> {
                        val updatedList = _timerList.value.map { timer ->
                            timer.copy(isSelected = false)
                        }
                        _timerList.value = updatedList

                        viewModelScope.launch {
                            _events.emit(MainUiEvent.TimerFinished)
                        }
                    }
                }
            }
        }
    }
    fun selectTimer(selectedIndex: Int) {
        val updatedList = _timerList.value.mapIndexed { index, timer ->
            timer.copy(isSelected = index == selectedIndex)
        }
        _timerList.value = updatedList
    }

    fun getSelectedTimer(): TimerModel? {
        return _timerList.value.find { it.isSelected }
    }

    fun formatTime(ms: Long, set: String): String {
        val hours = ms / 3600000
        val minutes = ms % 3600000 / 60000
        val seconds = ms % 3600000 % 60000 / 1000
        return String.format("$set %02d:%02d:%02d", hours, minutes, seconds)
    }
}