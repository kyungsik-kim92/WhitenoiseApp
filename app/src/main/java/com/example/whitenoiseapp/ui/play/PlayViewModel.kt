package com.example.whitenoiseapp.ui.play

import androidx.lifecycle.ViewModel
import com.example.whitenoiseapp.constants.Constants
import com.example.whitenoiseapp.model.PlayModel
import com.example.whitenoiseapp.service.WhiteNoiseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayViewModel : ViewModel() {
    private val _playList = MutableStateFlow<List<PlayModel>>(emptyList())
    val playList = _playList.asStateFlow()

    init {
        _playList.value = Constants.getPlayList()
    }

    fun clearSelection(timerState: Flow<WhiteNoiseService.TimerState>) {
        _playList.update { currentList ->
            currentList.map { playList ->
                playList.apply {
                    setIsSelected(false)
                }
            }
        }
    }
}