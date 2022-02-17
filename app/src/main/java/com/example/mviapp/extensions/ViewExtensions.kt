package com.example.mviapp.extensions

import android.view.View

fun View.changeVisibility(visible: Boolean) { visibility = if (visible) View.VISIBLE else View.GONE }