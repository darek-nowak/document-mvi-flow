package com.example.mviapp

import android.app.Application
import com.example.mviapp.di.ApplicationComponent
import com.example.mviapp.di.ApplicationModule
import com.example.mviapp.di.DaggerApplicationComponent
import timber.log.Timber

open class MviApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createApplicationComponent()
        initializeTimer()
    }

    open fun createApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule("https://api.github.com/"))
            .build()
    }

    private fun initializeTimer() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }
}