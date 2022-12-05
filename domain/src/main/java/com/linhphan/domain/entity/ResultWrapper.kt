package com.linhphan.domain.entity

/**
 * @author linhphan
 * a wrapper class for wrapping data transferring from lower layer up to higher layer.
 */
sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T): ResultWrapper<T>()
    data class GenericError(val code: Int, val message: String = "Unknown Error"): ResultWrapper<Nothing>()
    object InProgress : ResultWrapper<Nothing>()
//    object NetworkError: ResultWrapper<Nothing>()

    fun notProgress(): Boolean {
        return this !is InProgress
    }
}