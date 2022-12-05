package com.linhphan.presentation.feature.home.viewholder

import android.view.ViewGroup
import com.linhphan.presentation.R
import com.linhphan.presentation.base.BaseViewHolder
import com.linhphan.presentation.base.BaseViewHolderFactory
import com.linhphan.presentation.callback.OnItemClickListener
import com.linhphan.presentation.extensions.inflate

class ForecastViewHolderFactory: BaseViewHolderFactory {
    companion object{
        const val VIEW_TYPE_DAILY_FORECAST = 1
    }

    override fun createViewHolder(
        viewGroup: ViewGroup,
        viewType: Int,
        listener: OnItemClickListener?
    ): BaseViewHolder {
        return when(viewType){
            VIEW_TYPE_DAILY_FORECAST -> {
                ForecastViewHolder(viewGroup.inflate(R.layout.lp_item_forecast), listener)
            }
            else -> {
                ForecastViewHolder(viewGroup.inflate(R.layout.lp_item_forecast), listener)
            }
        }
    }
}