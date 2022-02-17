package com.example.mviapp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

suspend fun <T : Any> asResult(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    dataFetch: suspend () -> T
): Result<T> = withContext(dispatcher) {
    try {
        Result.Success(dataFetch())
    } catch (e: Exception) {
        Result.Error
    }
}

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    object Error : Result<Nothing>()
}