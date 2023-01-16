package com.example.composition.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.composition.R
import com.example.composition.domain.entity.Level
import com.example.composition.domain.usecases.GenerateQuestionUseCase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}