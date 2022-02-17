package com.example.mviapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

fun Activity.setUpAppBar(titleText: String, homeEnabled: Boolean = false) {
    (this as AppCompatActivity).supportActionBar?.apply {
        title = titleText
        setDisplayHomeAsUpEnabled(homeEnabled)
    }
}