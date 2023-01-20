package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

interface OnOptionClickListener {
    fun onOptionClick(option: Int)
}

@BindingAdapter("answersProgressColor")
fun bindAnswersProgressColor(textView: TextView, state: Boolean) {
    textView.setTextColor(getColorByState(textView.context, state))
}

@BindingAdapter("progressBar")
fun bindProgressBar(progressBar: ProgressBar, percentOfRightAnswers: Int) {
    progressBar.setProgress(percentOfRightAnswers, true)
}

@BindingAdapter("progressBarColor")
fun bindProgressBarColor(progressBar: ProgressBar, state: Boolean) {
    progressBar.progressTintList = ColorStateList.valueOf(getColorByState(progressBar.context, state))
}

private fun getColorByState(context: Context, state: Boolean): Int {
    val colorResId = if (state) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}

