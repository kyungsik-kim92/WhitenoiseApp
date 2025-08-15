package com.example.whitenoiseapp.data.datasource.local

import com.example.whitenoiseapp.domain.model.PlayModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class PlayLocalDataSource @Inject constructor(
    @Named("PlayList") private val initialPlayList: List<PlayModel>
) {

    private val _playList = MutableStateFlow(initialPlayList)
    val playList = _playList.asStateFlow()

    fun getPlayList(): Flow<List<PlayModel>> = playList

    fun updatePlaySelection(index: Int, isSelected: Boolean) {
        val currentList = _playList.value.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = currentList[index].copy(isSelected = isSelected)
            _playList.value = currentList
        }
    }

    fun clearAllSelections() {
        val clearedList = _playList.value.map { it.copy(isSelected = false) }
        _playList.value = clearedList
    }
}
