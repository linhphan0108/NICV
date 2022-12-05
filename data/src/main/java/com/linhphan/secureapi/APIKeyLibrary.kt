package com.linhphan.secureapi

object APIKeyLibrary {
    /**
     * A native method that is implemented by the 'secureapi' native library,
     * which is packaged with this application.
     */
    external fun getAppId(): String
    external fun getSSLCerts(): Any

    // Used to load the 'secureapi' library on application startup.
    init {
        System.loadLibrary("secureapi")
    }
}