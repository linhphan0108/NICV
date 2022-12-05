package com.linhphan.presentation.util.connection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData

class ConnectionLiveData(private val context: Context) : LiveData<ConnectionState>() {
    override fun onActive() {
        super.onActive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.applicationContext.registerReceiver(networkReceiver, filter)
    }

    override fun onInactive() {
        super.onInactive()
        context.applicationContext.unregisterReceiver(networkReceiver)
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        if(hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                            result = 3
                        }
                        if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            result = 2
                        } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            result = 1
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        when (type) {
                            ConnectivityManager.TYPE_ETHERNET -> {
                                result = 3
                            }
                            ConnectivityManager.TYPE_WIFI -> {
                                result = 2
                            }
                            ConnectivityManager.TYPE_MOBILE -> {
                                result = 1
                            }
                        }
                    }
                }
            }

            val state = when(result){
                0 -> ConnectionState(ConnectionState.CONNECTION_UNKNOWN, false)
                1 -> ConnectionState(ConnectionState.CONNECTION_MOBILE, true)
                2 -> ConnectionState(ConnectionState.CONNECTION_WIFI, true)
                3 -> ConnectionState(ConnectionState.CONNECTION_ETHERNET, true)
                else -> ConnectionState(ConnectionState.CONNECTION_UNKNOWN, false)
            }

            postValue(state)
        }
    }
}