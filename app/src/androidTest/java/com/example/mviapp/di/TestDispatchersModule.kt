package com.example.mviapp.di

import com.example.mviapp.CoroutineDispatchersProvider
import com.example.mviapp.coroutines.TestCoroutineDispatchersProvider
import dagger.Module
import dagger.Provides

@Module
class TestDispatchersModule: DispatchersModule() {
    @Provides
    override fun provideCoroutineDispatchersProvider(): CoroutineDispatchersProvider = TestCoroutineDispatchersProvider()
}