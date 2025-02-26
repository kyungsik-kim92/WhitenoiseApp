package com.example.whitenoiseapp.ui.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitenoiseapp.MainViewModel
import com.example.whitenoiseapp.constants.Constants
import com.example.whitenoiseapp.model.PlayModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayViewModel : ViewModel() {
    private val _playList = MutableStateFlow<List<PlayModel>>(emptyList())
    val playList = _playList.asStateFlow()

    init {
        _playList.value = Constants.getPlayList()
    }

    private fun clearSelection() {
        _playList.update { currentList ->
            currentList.map { playList ->
                playList.apply {
                    setIsSelected(false)
                }
            }
        }
    }

    fun observeSharedEvents(mainViewModel: MainViewModel) {
        viewModelScope.launch {
            mainViewModel.timerFinishedEvent.collect {
                clearSelection()
            }
        }
    }
}