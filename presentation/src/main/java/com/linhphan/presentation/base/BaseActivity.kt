package com.linhphan.presentation.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.linhphan.presentation.R
import com.linhphan.presentation.feature.popup.SingleActionConfirmationPopup
import com.linhphan.presentation.util.connection.ConnectionState
import java.io.File
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutId: Int

    private var connectionState: ConnectionState =
        ConnectionState(
            ConnectionState.CONNECTION_HAS_NOT_CHECK,
            false
        )

    protected val binding: T by lazy { getViewDataBindingInstance() }
    protected val viewModel: V by lazy { getViewModelInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initDataBinding()
        setupViews()
        setupObservers()
    }

    protected abstract fun getBindingVariableId(): Int
    protected open fun initData() {}
    protected open fun setupViews() {}
    open fun setupObservers() {
        viewModel.listenNetworkState(this, this)
        viewModel.networkConnectionState?.observe(this, { networkState ->
            if (networkState.isConnected) {
                hideNoNetworkConnectionPopup()
            } else {
                showNoNetworkConnectionPopup()
            }
        })
    }

    protected open fun getViewDataBindingInstance(): T {
        return DataBindingUtil.setContentView(this, layoutId)
    }

    protected open fun getViewModelInstance(): V {
        val typeToken = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<V>
        return ViewModelProvider(this).get(typeToken)
    }

    protected open fun setBindingVariables() {
        if (getBindingVariableId() > 0) {
            binding.setVariable(getBindingVariableId(), viewModel)
        }
    }

    private fun initDataBinding() {
        setBindingVariables()
        binding.lifecycleOwner = this@BaseActivity
    }

    //#region network
    private var noNetworkPopup: SingleActionConfirmationPopup? = null
    private fun showNoNetworkConnectionPopup(){
        if (noNetworkPopup?.isShowing == true){
            return
        }
        noNetworkPopup = SingleActionConfirmationPopup.Builder().apply {
            setTitle(getString(R.string.lp_title_popup_error))
            setMessage(getString(R.string.lp_message_error_no_network_connection))
            setConfirm(getString(R.string.lp_label_ok))
        }.build()
        noNetworkPopup!!.show(this)
    }

    private fun hideNoNetworkConnectionPopup(){
        noNetworkPopup?.dismiss()
        noNetworkPopup = null
    }
    //#endregion network

    protected fun showPopupRootedDevice(){
        SingleActionConfirmationPopup.Builder().apply {
            setCancelable(false)
            setTitle(getString(R.string.lp_title_popup_error))
            setMessage(getString(R.string.lp_message_error_rooted_device))
            setConfirm(getString(R.string.lp_label_ok)){
                finish()
            }
        }.build().show(this)
    }
}
