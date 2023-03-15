package com.example.composition.domain.usecases

import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository
import javax.inject.Inject

class GenerateQuestionUseCase @Inject constructor(private val repository: GameRepository) {

    operator fun invoke(maxSumValue: Int) =
        repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)

    companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}