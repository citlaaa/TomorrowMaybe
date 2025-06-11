package com.example.tomorrowmaybe.view.adapter

import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter

@BindingAdapter("app:tint")
fun setImageTint(imageView: ImageView, @ColorInt color: Int) {
    imageView.setColorFilter(color)
}