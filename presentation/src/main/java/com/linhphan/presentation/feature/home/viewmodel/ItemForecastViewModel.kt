package com.linhphan.presentation.feature.home.viewmodel

import com.linhphan.presentation.base.BaseItemViewModel
import com.linhphan.presentation.callback.OnItemClickListener
import com.linhphan.presentation.model.ForecastModel


class ItemForecastViewModel(listener: OnItemClickListener?) :
    BaseItemViewModel<ForecastModel>() {

    init {
        onItemClickListener = listener
    }
}