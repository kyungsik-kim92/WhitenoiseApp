package com.example.whitenoiseapp

sealed class MainUiState {
    data object Loading : MainUiState()
    data class Success(
        val isServiceReady: Boolean = false,
        val currentRealTime: Long = 0L
    ) : MainUiState()
    data class Error(val message: String) : MainUiState()
}

sealed class MainUiEvent {
    data object ServiceReady : MainUiEvent()
    data object TimerFinished : MainUiEvent()
    data class ShowError(val message: String) : MainUiEvent()
}