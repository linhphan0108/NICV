package com.linhphan.presentation.base

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.linhphan.common.Logger
import com.linhphan.presentation.extensions.distinctUntilChanged
import com.linhphan.presentation.util.connection.ConnectionLiveData
import com.linhphan.presentation.util.connection.ConnectionState

abstract class BaseViewModel: ViewModel() {
    companion object{
        private const val tag = "BaseViewModel"
    }
    private val _connectionState: MutableLiveData<ConnectionState> =
        MutableLiveData(
            ConnectionState(ConnectionState.CONNECTION_HAS_NOT_CHECK,false)
        )
    var networkConnectionState : LiveData<ConnectionState>? = null


    fun listenNetworkState(context: Context, owner: LifecycleOwner) : LiveData<ConnectionState> {
        if (networkConnectionState == null) {
            networkConnectionState = ConnectionLiveData(context).distinctUntilChanged()
            networkConnectionState?.observe(owner, { newConnectionState ->
                    Logger.e(tag, "network state = ${newConnectionState.type}")
                    _connectionState.postValue(newConnectionState)
                })
        }
        return _connectionState
    }

    fun adjustFontScale(activity: Activity, scaleFactor: Float) {
        val configuration = activity.resources.configuration
        configuration.fontScale = scaleFactor
        val metrics = activity.resources.displayMetrics
        val wm = activity.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        activity.baseContext.createConfigurationContext(configuration)
    }
}