package com.linhphan.presentation.feature.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.linhphan.presentation.base.BaseAdapter
import com.linhphan.presentation.model.ForecastModel
import com.linhphan.presentation.base.BaseViewHolderFactory
import com.linhphan.presentation.callback.OnItemClickListener
import com.linhphan.presentation.feature.home.viewholder.ForecastViewHolderFactory

class ForecastAdapter(
    onItemClickListener: OnItemClickListener? = null
) : BaseAdapter<ForecastModel>(onItemClickListener) {

    override fun getViewHolderFactory(): BaseViewHolderFactory {
        return ForecastViewHolderFactory()
    }

    override fun getDiffItem(): DiffUtil.ItemCallback<ForecastModel> {
        return object : DiffUtil.ItemCallback<ForecastModel>() {
            override fun areItemsTheSame(
                oldItem: ForecastModel,
                newItem: ForecastModel
            ): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(
                oldItem: ForecastModel,
                newItem: ForecastModel
            ): Boolean {
                return oldItem.date != newItem.date && oldItem.avgTemp != newItem.avgTemp
                        && oldItem.pressure != newItem.pressure && oldItem.humidity != newItem.humidity
            }
        }
    }

}