package com.example.whitenoiseapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _playList = MutableStateFlow<List<PlayModel>>(emptyList())
    val playList = _playList.asStateFlow()

    private val _timerList = MutableStateFlow<List<TimerModel>>(emptyList())
    val timerList = _timerList.asStateFlow()

    init {
        _playList.value = Constants.getPlayList()
        _timerList.value = Constants.getTimerList()
    }
}