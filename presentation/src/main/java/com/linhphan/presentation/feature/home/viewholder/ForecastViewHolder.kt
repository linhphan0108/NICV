package com.linhphan.presentation.feature.home.viewholder

import android.view.View
import com.linhphan.presentation.base.BaseViewHolder
import com.linhphan.presentation.callback.OnItemClickListener
import com.linhphan.presentation.databinding.LpItemForecastBinding
import com.linhphan.presentation.feature.home.viewmodel.ItemForecastViewModel
import com.linhphan.presentation.model.ForecastModel

class ForecastViewHolder(itemView: View, listener: OnItemClickListener?) :
    BaseViewHolder(itemView) {

    init {
        (binding as LpItemForecastBinding).viewModel = ItemForecastViewModel(listener)
    }

    override fun <T> bindData(data: T) {
        (binding as LpItemForecastBinding).viewModel?.dataObservable?.value = data as ForecastModel
        super.bindData(data)
    }

    override fun <T> bindData(data: T, payLoad: List<Any>) {
        (binding as LpItemForecastBinding).viewModel?.dataObservable?.value = data as ForecastModel
        super.bindData(data, payLoad)
    }
}