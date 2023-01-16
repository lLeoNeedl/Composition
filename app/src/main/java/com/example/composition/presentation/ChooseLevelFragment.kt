package com.example.composition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.composition.R
import com.example.composition.databinding.FragmentChoseLevelBinding
import com.example.composition.databinding.FragmentWelcomeBinding

class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChoseLevelBinding? = null
    private val binding: FragmentChoseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChoseLevelBinding == null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}