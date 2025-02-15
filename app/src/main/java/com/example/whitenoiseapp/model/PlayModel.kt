package com.example.whitenoiseapp.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PlayModel(
    var musicResId: Int,
    val iconResId: Int,
    private val _isSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
) {
    val isSelected: StateFlow<Boolean> = _isSelected.asStateFlow()

    fun setIsSelected(selected: Boolean) {
        _isSelected.value = selected
    }
}