package com.example.whitenoiseapp.core.utils

object TimeFormatter {

    fun formatTime(ms: Long): String {
        val hours = ms / 3600000
        val minutes = ms % 3600000 / 60000
        val seconds = ms % 3600000 % 60000 / 1000
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun formatTimeWithPrefix(ms: Long, prefix: String): String {
        val timeString = formatTime(ms)
        return if (prefix.isNotEmpty()) {
            "$prefix $timeString"
        } else {
            timeString
        }
    }
}