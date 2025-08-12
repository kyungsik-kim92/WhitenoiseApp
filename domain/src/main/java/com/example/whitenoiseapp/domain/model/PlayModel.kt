package com.example.whitenoiseapp.domain.model

data class PlayModel(
    val musicResId: Int,
    val iconResId: Int,
    var isSelected: Boolean = false
)
