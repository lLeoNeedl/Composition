package com.example.composition.presentation.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.composition.R
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    repository: GameRepository
) : ViewModel() {


    private val level by lazy {
        savedStateHandle.get<Level>(LEVEL_KEY)
            ?: throw RuntimeException("Param level is absent")
    }

    private lateinit var gameSettings: GameSettings

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null

    private var _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private var _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _progressOfAnswers = MutableLiveData<String>()
    val progressOfAnswers: LiveData<String>
        get() = _progressOfAnswers

    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughCountOfRightAnswers

    private val _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswers: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswers

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private var _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var numberOfQuestions = 0
    private var numberOfRightAnswers = 0

    init {
        startGame()
    }

    fun saveLevel(level: Level) {
        savedStateHandle[LEVEL_KEY] = level
    }

    private fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
        updateProgress()
    }

    private fun getGameSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    fun checkAnswer(answer: Int) {
        val rightAnswer = _question.value?.rightAnswer
        if (answer == rightAnswer) {
            numberOfRightAnswers++
        }
        numberOfQuestions++
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightsAnswers()
        _percentOfRightAnswers.value = percent
        _progressOfAnswers.value = String.format(
            application.resources.getString(R.string.progress_answers),
            numberOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
        _enoughCountOfRightAnswers.value =
            numberOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercentOfRightAnswers.value =
            percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightsAnswers(): Int {
        if (numberOfQuestions == 0) {
            return 0
        }
        return (numberOfRightAnswers / numberOfQuestions.toDouble() * 100).toInt()
    }

    private fun startTimer() {
        val millis = gameSettings.gameInSeconds * MILLIS_IN_SECONDS
        timer = object : CountDownTimer(millis, MILLIS_IN_SECONDS) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun formatTime(millisUntilFinished: Long): String {
        var seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        return String.format("%02d:%02d", minutes, ++seconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            _enoughCountOfRightAnswers.value == true &&
                    _enoughPercentOfRightAnswers.value == true,
            numberOfRightAnswers,
            numberOfQuestions,
            gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {

        const val LEVEL_KEY = "level"

        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}