package com.example.mviapp

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class ViewModelInstrumentationRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return Instrumentation.newApplication(TestMviApplication::class.java, context)
    }
}