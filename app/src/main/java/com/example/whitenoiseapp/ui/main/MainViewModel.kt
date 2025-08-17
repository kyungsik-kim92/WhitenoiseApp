package com.example.whitenoiseapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitenoiseapp.core.extensions.stateInDefault
import com.example.whitenoiseapp.domain.model.TimerState
import com.example.whitenoiseapp.domain.usecase.FormatTimeUseCase
import com.example.whitenoiseapp.domain.usecase.GetTimerListUseCase
import com.example.whitenoiseapp.domain.usecase.ObserveTimerStateUseCase
import com.example.whitenoiseapp.domain.usecase.SelectTimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTimerListUseCase: GetTimerListUseCase,
    private val selectTimerUseCase: SelectTimerUseCase,
    private val observeTimerStateUseCase: ObserveTimerStateUseCase,
    private val formatTimeUseCase: FormatTimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MainUiEvent>()
    val events = _events.asSharedFlow()

    val timerList = getTimerListUseCase().stateInDefault(viewModelScope, emptyList())

    private val _realTime = MutableStateFlow(0L)
    val realTime = _realTime.asStateFlow()

    init {
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

    fun observeTimerState() {
        viewModelScope.launch {
            observeTimerStateUseCase().collect { state ->
                when (state) {
                    is TimerState.Update -> {
                        _realTime.value = state.ms
                        val currentState = _uiState.value
                        if (currentState is MainUiState.Success) {
                            _uiState.value = currentState.copy(currentRealTime = state.ms)
                        }
                    }

                    is TimerState.Paused -> {
                        _realTime.value = state.ms
                        val currentState = _uiState.value
                        if (currentState is MainUiState.Success) {
                            _uiState.value = currentState.copy(currentRealTime = state.ms)
                        }
                        viewModelScope.launch {
                            _events.emit(MainUiEvent.TimerPaused)
                        }
                    }

                    is TimerState.Resumed -> {
                        _realTime.value = state.ms
                        val currentState = _uiState.value
                        if (currentState is MainUiState.Success) {
                            _uiState.value = currentState.copy(currentRealTime = state.ms)
                        }
                        viewModelScope.launch {
                            _events.emit(MainUiEvent.TimerResumed)
                        }
                    }

                    is TimerState.Finish -> {
                        viewModelScope.launch {
                            _events.emit(MainUiEvent.TimerFinished)
                        }
                    }

                }
            }
        }
    }

    fun selectTimer(selectedIndex: Int) {
        viewModelScope.launch {
            val result = selectTimerUseCase(selectedIndex)
            result.onFailure { exception ->
                _events.emit(MainUiEvent.ShowError(exception.message ?: "Error Message"))
            }
        }
    }

    fun formatTime(ms: Long, set: String): String {
        return formatTimeUseCase(ms, set)
    }

}