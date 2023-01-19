package com.example.composition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.composition.R
import com.example.composition.databinding.FragmentChoseLevelBinding
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult
import kotlin.math.roundToInt

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(GAME_RESULT_KEY)?.let {
            gameResult = it
        }
    }

    private fun setClickListeners() {
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun setViews() {
        if (gameResult.winner) {
            binding.emojiResult.setImageResource(R.drawable.ic_smile)
        } else {
            binding.emojiResult.setImageResource(R.drawable.ic_sad)
        }
        with(binding) {
            tvRequiredAnswers.text = String.format(
                resources.getString(R.string.required_score),
                gameResult.gameSettings.minCountOfRightAnswers
            )
            tvRequiredPercentage.text = String.format(
                resources.getString(R.string.required_percentage),
                gameResult.gameSettings.minPercentOfRightAnswers
            )
            tvScoreAnswers.text = String.format(
                resources.getString(R.string.score_answers),
                gameResult.countOfRightAnswers
            )
            tvScorePercentage.text = String.format(
                resources.getString(R.string.score_percentage),
                calculatePercentOfRightAnswers()
            )
        }
    }

    private fun calculatePercentOfRightAnswers(): Int {
        return if (gameResult.countOfQuestions == 0) {
            0
        } else {
            (gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble() * 100).toInt()
        }
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

    companion object {

        const val GAME_RESULT_KEY = "game_result"

        fun newInstance(gameResult: GameResult) =
            GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GAME_RESULT_KEY, gameResult)
                }
            }
    }
}