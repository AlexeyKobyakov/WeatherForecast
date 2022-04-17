package com.alexeykov.weather.model.cloud

import com.alexeykov.weather.model.NoNetworkConnection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.lang.Exception

suspend fun <T> wrapNetworkException(dispatcher: CoroutineDispatcher, block: suspend CoroutineScope.() -> T): T {
    try {
        return withContext(dispatcher, block)
    } catch (e: Exception) {
        val appException = NoNetworkConnection()
        appException.initCause(e)
        throw appException
    }
}