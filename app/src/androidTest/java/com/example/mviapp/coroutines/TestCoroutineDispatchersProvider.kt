package com.example.mviapp.coroutines

import android.os.AsyncTask
import com.example.mviapp.CoroutineDispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher

class TestCoroutineDispatchersProvider: CoroutineDispatchersProvider {
    override fun  io(): CoroutineDispatcher = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher()
}