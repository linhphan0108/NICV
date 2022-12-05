package com.linhphan.presentation.feature.home.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.linhphan.domain.entity.ResultWrapper
import com.linhphan.presentation.BR
import com.linhphan.presentation.R
import com.linhphan.presentation.base.BaseActivity
import com.linhphan.presentation.databinding.ActivityMainBinding
import com.linhphan.presentation.extensions.gone
import com.linhphan.presentation.extensions.hideKeyboard
import com.linhphan.presentation.extensions.visible
import com.linhphan.presentation.feature.home.adapter.ForecastAdapter
import com.linhphan.presentation.feature.home.viewmodel.MainViewModel
import com.linhphan.presentation.model.ForecastModel
import dagger.hilt.android.AndroidEntryPoint
import android.view.Menu
import android.view.MenuItem
import com.linhphan.common.Logger
import com.linhphan.presentation.extensions.isRootedDevice
import com.linhphan.presentation.feature.popup.textSizePopup.TextSizePopup

private const val BUNDLE_KEY_QUERY = "BUNDLE_KEY_QUERY"
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    companion object{
        private const val tag = "MainActivity"
        fun createIntent(context: Context, query: String?): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(BUNDLE_KEY_QUERY, query)
            }
        }
    }

    private var foreCastAdapter = ForecastAdapter()

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun getBindingVariableId(): Int {
        return BR.viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isRootedDevice()){
            showPopupRootedDevice()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.lp_menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.text_size){
            showTextSizePopup()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initData() {
        val lastScaleFactor = viewModel.getLastTextScaleFactor() * 0.01f
        Logger.d(tag, "last scale factor = $lastScaleFactor")
        viewModel.adjustFontScale(this, lastScaleFactor)
        viewModel.resumeLastQuery(this, intent.extras?.getString(BUNDLE_KEY_QUERY))
    }

    override fun setupViews() {
        binding.edtCity.setText(intent.extras?.getString(BUNDLE_KEY_QUERY) ?: "")
        setupForecastRecycleView()
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.onGetWeatherClickObservable.observe(this, {
            onGetWeatherButtonClicked()
        })
        viewModel.forecastsObservable.observe(this, {
            onGetForecastResult(it)
        })
        viewModel.onTextSizeChangeObservable.observe(this, {
            viewModel.restartApplication(this, binding.edtCity.text.toString().trim())
        })
    }

    private fun setupForecastRecycleView(){
        binding.recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = foreCastAdapter
        }
    }

    private fun onGetWeatherButtonClicked(){
        val q = binding.edtCity.text.toString().trim()
        getForecasts(q)
        hideKeyboard()
    }

    private fun getForecasts(cityName: String){
        viewModel.fetchForecasts(this, cityName)
    }

    private fun onGetForecastResult(resultWrapper: ResultWrapper<List<ForecastModel>>) {
        when(resultWrapper){
            is ResultWrapper.GenericError -> {
                onGetForecastError(resultWrapper.code, resultWrapper.message)
                binding.progressBar.gone()
            }
            ResultWrapper.InProgress -> {
                binding.progressBar.visible()
            }
            is ResultWrapper.Success -> {
                onGetForecastSuccess(resultWrapper.data)
                binding.progressBar.gone()
            }
        }
    }

    private fun onGetForecastSuccess(data: List<ForecastModel>) {
        if (data.isEmpty()){
            binding.layoutError.visible()
            binding.tvMessage.text = getString(R.string.lp_message_empty_data)
        }else{
            binding.layoutError.gone()
        }
        foreCastAdapter.setData(data)
    }

    private fun onGetForecastError(code: Int, message: String) {
        binding.layoutError.visible()
        binding.tvMessage.text = getString(R.string.lp_message_error_holder, message)
        foreCastAdapter.clear()
    }

    private fun showTextSizePopup(){
        TextSizePopup.show(supportFragmentManager)
    }

}