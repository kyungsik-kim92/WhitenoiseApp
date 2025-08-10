package com.example.whitenoiseapp.ui.main

sealed class MainUiState {
    data object Loading : MainUiState()
    data class Success(
        val isServiceReady: Boolean = false,
        val currentRealTime: Long = 0L
    ) : MainUiState()
}

sealed class MainUiEvent {
    data object ServiceReady : MainUiEvent()
    data object TimerFinished : MainUiEvent()
    data object TimerPaused : MainUiEvent()
    data object TimerResumed : MainUiEvent()
    data class ShowError(val message: String) : MainUiEvent()
}