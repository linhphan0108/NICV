package com.linhphan.presentation.feature.popup.textSizePopup

import android.content.Context
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.linhphan.presentation.base.BaseViewModel
import com.linhphan.presentation.common.Constants.DEFAULT_SCALE_FACTOR_PERCENT
import com.linhphan.presentation.common.Constants.MIN_TEXT_SCALE_FACTOR
import com.linhphan.presentation.extensions.distinctUntilChanged
import com.linhphan.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextSizePopupViewModel @Inject constructor(): BaseViewModel() {
    private val _textScaleProgress = MutableLiveData(DEFAULT_SCALE_FACTOR_PERCENT)
    val textScaleProgressObservable = _textScaleProgress.distinctUntilChanged()
    private val _textScale = MutableLiveData(0)
    val textScaleObservable = _textScale.distinctUntilChanged()
    private val _textSize = MutableLiveData(0f)
    val textSizeObservable = _textSize.distinctUntilChanged()
    private var defaultTextSize = 0f
    private val _applyTextScaleButtonState = MutableLiveData(false)
    val applyTextScaleButtonState = _applyTextScaleButtonState as LiveData<Boolean>
    private val _onApplyTextSize = SingleLiveEvent<Int>()
    val onApplyTextSizeObservable = _onApplyTextSize.distinctUntilChanged() as LiveData<Int>

    fun setDefaultTextSize(context: Context){
        val fontScale = context.resources.configuration.fontScale
        val txt = TextView(context)
        val scaledFactorPer = (fontScale * 100).toInt()
        defaultTextSize = txt.textSize / fontScale
        _textSize.value = defaultTextSize * fontScale
        _textScale.value = scaledFactorPer
        _textScaleProgress.value = scaledFactorPer - MIN_TEXT_SCALE_FACTOR
    }

    fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean){
        if (progress == _textScaleProgress.value) return
        val expectedScale = progress * 0.01 + 0.5
        _applyTextScaleButtonState.value = true
        _textScale.value = progress + MIN_TEXT_SCALE_FACTOR
        _textScaleProgress.value = progress
        _textSize.value = (defaultTextSize * expectedScale).toFloat()
    }

    fun onApplyNewTextScale(){
        val factor = _textScale.value ?: DEFAULT_SCALE_FACTOR_PERCENT
        _onApplyTextSize.value = factor
    }
}