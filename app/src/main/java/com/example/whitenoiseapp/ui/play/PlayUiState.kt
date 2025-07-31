package com.example.whitenoiseapp.ui.play

import com.example.whitenoiseapp.model.PlayModel
import com.example.whitenoiseapp.model.TimerModel

sealed class PlayUiState {
    data object Loading : PlayUiState()

    data class Success(
        val playList: List<PlayModel> = emptyList(),
        val selectedPlays: List<PlayModel> = emptyList(),
        val isServiceReady: Boolean = false,
        val currentTimer: TimerModel? = null,
        val realTime: Long = 0L,
        val scheduledTime: Long = 0L,
        val isTimerVisible: Boolean = false
    ) : PlayUiState()

    data class Error(val message: String) : PlayUiState()
}

sealed class PlayUiEvent {
    data class PlaySelected(val index: Int, val isSelected: Boolean) : PlayUiEvent()
    data class ShowToast(val message: String) : PlayUiEvent()
    data object ClearAllSelection : PlayUiEvent()
    data object TimerFinished : PlayUiEvent()
}