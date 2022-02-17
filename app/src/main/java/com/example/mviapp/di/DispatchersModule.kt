package com.example.mviapp.di

import com.example.mviapp.CoroutineDispatchersProvider
import com.example.mviapp.DefaultCoroutineDispatchersProvider
import dagger.Module
import dagger.Provides

@Module
open class DispatchersModule {
    @Provides
    open fun provideCoroutineDispatchersProvider(): CoroutineDispatchersProvider = DefaultCoroutineDispatchersProvider()
}