package com.example.composition.domain.usecases

import com.example.composition.domain.entity.Level
import com.example.composition.domain.repository.GameRepository
import javax.inject.Inject

class GetGameSettingsUseCase @Inject constructor(private val repository: GameRepository) {
    operator fun invoke(level: Level) = repository.getGameSettings(level)
}