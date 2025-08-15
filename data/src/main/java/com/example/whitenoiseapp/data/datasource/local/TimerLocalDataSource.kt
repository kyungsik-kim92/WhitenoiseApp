package com.example.whitenoiseapp.data.datasource.local

import com.example.whitenoiseapp.domain.model.TimerModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TimerLocalDataSource @Inject constructor(
    @Named("TimerList") private val initialTimerList: List<TimerModel>
) {

    private val _timerList = MutableStateFlow(initialTimerList)
    val timerList = _timerList.asStateFlow()

    fun getTimerList(): Flow<List<TimerModel>> = timerList

    fun selectTimer(index: Int) {
        val currentList = _timerList.value.toMutableList()
        val updatedList = currentList.mapIndexed { i, timer ->
            timer.copy(isSelected = i == index)
        }
        _timerList.value = updatedList
    }

    fun getSelectedTimer(): Flow<TimerModel?> {
        return timerList.map { list -> list.find { it.isSelected } }
    }
}
