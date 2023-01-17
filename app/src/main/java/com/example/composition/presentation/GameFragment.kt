package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level

class GameFragment : Fragment() {

    private lateinit var level: Level

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private lateinit var gameViewModel: GameViewModel
    private lateinit var gameSettings: GameSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]
        gameSettings = gameViewModel.getGameSettings(level)
        observeViewModel()
        setupClickListeners()
        if (savedInstanceState == null) {
            gameViewModel.generateQuestion(gameSettings.maxSumValue)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    private fun observeViewModel() {
        gameViewModel.question.observe(viewLifecycleOwner) {
            with(binding) {
                tvSum.text = it.sum.toString()
                tvLeftNumber.text = it.visibleNumber.toString()
                tvOption1.text = it.options[0].toString()
                tvOption2.text = it.options[1].toString()
                tvOption3.text = it.options[2].toString()
                tvOption4.text = it.options[3].toString()
                tvOption5.text = it.options[4].toString()
                tvOption6.text = it.options[5].toString()
            }
        }
        gameViewModel.rightAnswers.observe(viewLifecycleOwner) {
            binding.tvAnswersProgress.text = String.format(
                getString(R.string.right_answers),
                it,
                gameSettings.minCountOfRightAnswers
            )
            binding.progressBar.progress = it
        }
        gameViewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.tvTimer.text = String.format("00:%02d", it)
        }
        gameViewModel.shouldOpenNextScreen.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(
                GameResult(
                    true,
                    20,
                    20,
                    GameSettings(
                        2,
                        2,
                        2,
                        2
                    )
                )
            )
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            tvOption1.setOnClickListener {
                val answer = convertAnswer(tvOption1)
                gameViewModel.checkAnswer(answer)
                gameViewModel.generateQuestion(gameSettings.maxSumValue)
            }
            tvOption2.setOnClickListener {
                val answer = convertAnswer(tvOption2)
                gameViewModel.checkAnswer(answer)
                gameViewModel.generateQuestion(gameSettings.maxSumValue)
            }
            tvOption3.setOnClickListener {
                val answer = convertAnswer(tvOption3)
                gameViewModel.checkAnswer(answer)
                gameViewModel.generateQuestion(gameSettings.maxSumValue)
            }
            tvOption4.setOnClickListener {
                val answer = convertAnswer(tvOption4)
                gameViewModel.checkAnswer(answer)
                gameViewModel.generateQuestion(gameSettings.maxSumValue)
            }
            tvOption5.setOnClickListener {
                val answer = convertAnswer(tvOption5)
                gameViewModel.checkAnswer(answer)
                gameViewModel.generateQuestion(gameSettings.maxSumValue)
            }
            tvOption6.setOnClickListener {
                val answer = convertAnswer(tvOption6)
                gameViewModel.checkAnswer(answer)
                gameViewModel.generateQuestion(gameSettings.maxSumValue)
            }
        }
    }

    private fun convertAnswer(textView: TextView) = textView.text.toString().toInt()

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    companion object {

        const val NAME = "GameFragment"
        private const val KEY_LEVEL = "Level"

        fun newInstance(level: Level) = GameFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_LEVEL, level)
            }
        }
    }
}
