package com.example.whitenoiseapp.di

import com.example.whitenoiseapp.constants.Constants
import com.example.whitenoiseapp.domain.model.PlayModel
import com.example.whitenoiseapp.domain.model.TimerModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    @Named("PlayList")
    fun providePlayList(): List<PlayModel> {
        return Constants.getPlayList()
    }

    @Provides
    @Singleton
    @Named("TimerList")
    fun provideTimerList(): List<TimerModel> {
        return Constants.getTimerList()
    }
}
