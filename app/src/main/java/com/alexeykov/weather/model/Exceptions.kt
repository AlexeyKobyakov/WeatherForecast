package com.alexeykov.weather.model

open class AppException : RuntimeException()

class NoNetworkConnection: AppException()

class StorageException: AppException()