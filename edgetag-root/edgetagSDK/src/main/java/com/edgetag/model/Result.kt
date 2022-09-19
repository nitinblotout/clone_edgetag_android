package com.edgetag.model

sealed class Result<out R> {
    data class Success<out T>(val data:T):Result<T>()
    data class Error(val errorData:InternalError):Result<Nothing>()
}
