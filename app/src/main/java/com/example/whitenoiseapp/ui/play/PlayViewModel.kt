package com.example.whitenoiseapp.ui.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitenoiseapp.MainUiEvent
import com.example.whitenoiseapp.MainViewModel
import com.example.whitenoiseapp.constants.Constants
import com.example.whitenoiseapp.model.PlayModel
import com.example.whitenoiseapp.model.TimerModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<PlayUiState>(PlayUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PlayUiEvent>()
    val events = _events.asSharedFlow()

    private val _playList = MutableStateFlow<List<PlayModel>>(emptyList())
    val playList = _playList.asStateFlow()

    init {
        loadPlayList()
    }

    private fun loadPlayList() {
        try {
            val playList = Constants.getPlayList()
            _playList.value = playList
            _uiState.value = PlayUiState.Success(
                playList = playList,
                selectedPlays = emptyList(),
                isServiceReady = false
            )
        } catch (e: Exception) {
            _uiState.value = PlayUiState.Error("Error Message")
        }
    }

    fun updateServiceReady(isReady: Boolean) {
        val currentState = _uiState.value
        if (currentState is PlayUiState.Success) {
            _uiState.value = currentState.copy(isServiceReady = isReady)
        }
    }

    fun updateTimerInfo(selectedTimer: TimerModel?, realTime: Long) {
        val currentState = _uiState.value
        if (currentState is PlayUiState.Success) {
            _uiState.value = currentState.copy(
                currentTimer = selectedTimer,
                realTime = realTime,
                scheduledTime = selectedTimer?.ms ?: 0L,
                isTimerVisible = selectedTimer != null && selectedTimer.isSelected
            )
        }
    }

    fun togglePlaySelection(index: Int) {
        val currentList = _playList.value.toMutableList()
        val currentItem = currentList[index]

        currentList[index] = currentItem.copy(isSelected = !currentItem.isSelected)
        _playList.value = currentList

        val selectedPlays = currentList.filter { it.isSelected }
        val currentState = _uiState.value
        if (currentState is PlayUiState.Success) {
            _uiState.value = currentState.copy(
                playList = currentList,
                selectedPlays = selectedPlays
            )
        }

        viewModelScope.launch {
            _events.emit(PlayUiEvent.PlaySelected(index, currentList[index].isSelected))
        }
    }

    fun observeTimerFinished(mainViewModel: MainViewModel) {
        viewModelScope.launch {
            mainViewModel.events.collect { event ->
                if (event is MainUiEvent.TimerFinished) {
                    clearAllSelection()
                }
            }
        }
    }

    private fun clearAllSelection() {
        val clearedList = _playList.value.map { play ->
            play.copy(isSelected = false)
        }
        _playList.value = clearedList

        val currentState = _uiState.value
        if (currentState is PlayUiState.Success) {
            _uiState.value = currentState.copy(
                playList = clearedList,
                selectedPlays = emptyList(),
                isTimerVisible = false
            )
        }

        viewModelScope.launch {
            _events.emit(PlayUiEvent.ClearAllSelection)
            _events.emit(PlayUiEvent.TimerFinished)
        }
    }

}