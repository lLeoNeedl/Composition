package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {

    private val args by navArgs<GameFinishedFragmentArgs>()

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")


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

    private fun setClickListeners() {
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun setViews() {
        binding.gameResult = args.gameResult
        if (args.gameResult.winner) {
            binding.emojiResult.setImageResource(R.drawable.ic_smile)
        } else {
            binding.emojiResult.setImageResource(R.drawable.ic_sad)
        }
        with(binding) {
//            tvRequiredAnswers.text = String.format(
//                resources.getString(R.string.required_score),
//                gameResult.gameSettings.minCountOfRightAnswers
//            )
//            tvRequiredPercentage.text = String.format(
//                resources.getString(R.string.required_percentage),
//                gameResult.gameSettings.minPercentOfRightAnswers
//            )
//            tvScoreAnswers.text = String.format(
//                resources.getString(R.string.score_answers),
//                gameResult.countOfRightAnswers
//            )
            tvScorePercentage.text = String.format(
                resources.getString(R.string.score_percentage),
                calculatePercentOfRightAnswers()
            )
        }
    }

    private fun calculatePercentOfRightAnswers(): Int {
        return if (args.gameResult.countOfQuestions == 0) {
            0
        } else {
            (args.gameResult.countOfRightAnswers / args.gameResult.countOfQuestions.toDouble() * 100).toInt()
        }
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }
}