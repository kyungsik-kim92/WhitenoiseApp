package com.example.whitenoiseapp.di

import com.example.whitenoiseapp.data.repository.MediaPlayerRepositoryImpl
import com.example.whitenoiseapp.data.repository.PlayRepositoryImpl
import com.example.whitenoiseapp.data.repository.TimerRepositoryImpl
import com.example.whitenoiseapp.domain.repository.MediaPlayerRepository
import com.example.whitenoiseapp.domain.repository.PlayRepository
import com.example.whitenoiseapp.domain.repository.TimerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPlayRepository(
        playRepositoryImpl: PlayRepositoryImpl
    ): PlayRepository

    @Binds
    abstract fun bindTimerRepository(
        timerRepositoryImpl: TimerRepositoryImpl
    ): TimerRepository

    @Binds
    abstract fun bindMediaPlayerRepository(
        mediaPlayerRepositoryImpl: MediaPlayerRepositoryImpl
    ): MediaPlayerRepository
}
