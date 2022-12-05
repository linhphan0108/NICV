package com.linhphan.presentation.util.connection

import androidx.annotation.IntDef

data class ConnectionState(
    @ConnectionType val type: Int,
    val isConnected: Boolean){


    companion object{
        const val CONNECTION_HAS_NOT_CHECK = -99
        const val CONNECTION_UNKNOWN = 10
        const val CONNECTION_MOBILE = 11
        const val CONNECTION_WIFI = 12
        const val CONNECTION_ETHERNET = 13
    }

    @IntDef(
        CONNECTION_HAS_NOT_CHECK,
        CONNECTION_UNKNOWN,
        CONNECTION_ETHERNET,
        CONNECTION_WIFI,
        CONNECTION_MOBILE
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ConnectionType
}
