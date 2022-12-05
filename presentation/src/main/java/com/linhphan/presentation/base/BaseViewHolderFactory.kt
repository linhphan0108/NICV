package com.linhphan.presentation.base

import android.view.ViewGroup
import com.linhphan.presentation.callback.OnItemClickListener

interface BaseViewHolderFactory {
    fun createViewHolder(
        viewGroup: ViewGroup,
        viewType: Int,
        listener: OnItemClickListener?
    ): BaseViewHolder
}