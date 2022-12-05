package com.linhphan.presentation.base

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected var binding: ViewDataBinding? = null

    init {
        if (binding == null) {
            binding = DataBindingUtil.bind(itemView)
            binding?.lifecycleOwner = itemView.context as? LifecycleOwner
        }
    }

    open fun <T> bindData(data: T) {
        binding?.executePendingBindings()
        binding?.invalidateAll()
    }

    open fun <T> bindData(data: T, payLoad: List<Any>) {
        binding?.executePendingBindings()
        binding?.invalidateAll()
    }

    open fun unbind() {
        binding?.unbind()
    }
}