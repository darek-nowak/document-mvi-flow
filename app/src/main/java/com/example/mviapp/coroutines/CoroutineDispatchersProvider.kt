package com.example.mviapp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoroutineDispatchersProvider {
    fun io(): CoroutineDispatcher
}

class DefaultCoroutineDispatchersProvider: CoroutineDispatchersProvider {
    override fun  io(): CoroutineDispatcher = Dispatchers.IO
}