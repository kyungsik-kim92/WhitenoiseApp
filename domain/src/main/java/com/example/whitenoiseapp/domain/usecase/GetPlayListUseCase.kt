package com.example.whitenoiseapp.domain.usecase

import com.example.whitenoiseapp.domain.model.PlayModel
import com.example.whitenoiseapp.domain.repository.PlayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlayListUseCase @Inject constructor(
    private val playRepository: PlayRepository
) {
    operator fun invoke(): Flow<List<PlayModel>> {
        return playRepository.getPlayList()
    }
}
