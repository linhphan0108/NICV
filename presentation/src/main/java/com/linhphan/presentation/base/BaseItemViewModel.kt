package com.linhphan.presentation.base

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.linhphan.common.Logger
import com.linhphan.presentation.callback.OnItemClickListener
import com.linhphan.presentation.model.Displayable

abstract class BaseItemViewModel<T: Displayable> : ViewModel() {

     var dataObservable: MutableLiveData<T> = MutableLiveData()
     var onItemClickListener: OnItemClickListener? = null

    open fun getData(): T? {
        return dataObservable.value
    }

    open fun onItemClicked(v: View) {
        onItemClickListener?.onItemClicked(getData())
        Logger.d("BaseItemViewModel", "onItemClicked | $v")
    }
}