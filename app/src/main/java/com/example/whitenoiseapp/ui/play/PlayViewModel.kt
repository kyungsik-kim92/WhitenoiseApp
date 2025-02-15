package com.example.whitenoiseapp.ui.play

import androidx.lifecycle.ViewModel
import com.example.whitenoiseapp.constants.Constants
import com.example.whitenoiseapp.model.PlayModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayViewModel : ViewModel() {
    private val _playList = MutableStateFlow<List<PlayModel>>(emptyList())
    val playList = _playList.asStateFlow()

    private var _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    init {
        _playList.value = Constants.getPlayList()
    }

    fun setIsPlaying(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }
}