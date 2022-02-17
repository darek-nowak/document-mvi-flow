package com.example.mviapp

import androidx.test.espresso.IdlingRegistry
import com.example.mviapp.di.ApplicationModule
import com.example.mviapp.di.DaggerApplicationComponent
import com.example.mviapp.di.TestDispatchersModule
import com.example.mviapp.rules.MOCK_WEBSERVER_PORT
import com.jakewharton.espresso.OkHttp3IdlingResource

class TestMviApplication : MviApplication() {

    override fun onCreate() {
        super.onCreate()
        registerIdlingResources()
    }

    override fun createApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule("http://localhost:$MOCK_WEBSERVER_PORT"))
            .dispatchersModule(TestDispatchersModule())
            .build()
    }

    private fun registerIdlingResources() {
        val okHttpIdlingResource = OkHttp3IdlingResource.create("OkHttp", applicationComponent.okHttpClient())
        IdlingRegistry.getInstance().register(
            okHttpIdlingResource
        )
    }
}