package com.alexeykov.weather.model

open class AppException : RuntimeException()

class NoDataException: AppException()

class StorageException: AppException()