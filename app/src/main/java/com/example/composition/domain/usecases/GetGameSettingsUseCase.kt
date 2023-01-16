package com.example.composition.domain.usecases

import com.example.composition.domain.entity.Level
import com.example.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {
    operator fun invoke(level: Level) = repository.getGameSettings(level)
}