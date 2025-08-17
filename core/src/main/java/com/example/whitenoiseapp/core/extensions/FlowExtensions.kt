package com.example.whitenoiseapp.core.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.stateInDefault(
    scope: CoroutineScope,
    initialValue: T
) = stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(5000L),
    initialValue = initialValue
)