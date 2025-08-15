package com.example.whitenoiseapp.data.repository

import com.example.whitenoiseapp.data.datasource.local.PlayLocalDataSource
import com.example.whitenoiseapp.domain.model.PlayModel
import com.example.whitenoiseapp.domain.repository.PlayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PlayRepositoryImpl @Inject constructor(
    private val playLocalDataSource: PlayLocalDataSource
) : PlayRepository {

    override fun getPlayList(): Flow<List<PlayModel>> {
        return playLocalDataSource.getPlayList()
    }

    override suspend fun togglePlaySelection(index: Int) {
        val currentList = playLocalDataSource.playList.first()
        if (index in currentList.indices) {
            val currentItem = currentList[index]
            playLocalDataSource.updatePlaySelection(index, !currentItem.isSelected)
        }
    }

    override suspend fun clearAllSelections() {
        playLocalDataSource.clearAllSelections()
    }
}
