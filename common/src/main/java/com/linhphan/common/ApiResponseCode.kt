package com.linhphan.common

object ApiResponseCode {
    //region general
    const val ERROR_UNKNOWN = -1
    const val ERROR_NETWORK = -2
    const val SUCCESS = 1
    const val SUCCESS_BUT_DATA_NULL = -99
    const val NOT_FOUND = 404 //not found
    const val AUTHORIZATION_INVALID = 400
    const val AUTHORIZATION_HAS_BEEN_DENIED = 401
}