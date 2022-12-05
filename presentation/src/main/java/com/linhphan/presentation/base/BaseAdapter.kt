package com.linhphan.presentation.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.linhphan.presentation.callback.OnItemClickListener
import com.linhphan.presentation.model.Displayable

abstract class BaseAdapter<T : Displayable>(
    private val onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<BaseViewHolder>() {

    private var items = ArrayList<T>()

    protected abstract fun getDiffItem() : DiffUtil.ItemCallback<T>
    protected abstract fun getViewHolderFactory() : BaseViewHolderFactory

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return getViewHolderFactory().createViewHolder(parent, viewType, onItemClickListener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.bindData(getItem(position), payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
        holder.unbind()
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    open fun setData(data: List<T>) {
        val newList = ArrayList(data)
        submit(newList)
    }

    private fun submit(items: List<T>) {
        autoNotify(this.items, items)
    }

    fun getItem(position: Int): T {
        return items[position]
    }

    fun addItems(items: List<T>) {
        if (this.items.isEmpty()) {
            setData(items)
        } else {
            this.items.addAll(items)
            notifyItemRangeInserted(itemCount, items.size)
        }
    }

    fun refreshData() {
        val newList = ArrayList(items)
        submit(newList)
    }

    fun clear() {
        submit(arrayListOf())
    }

    fun getData(): List<T> {
        return items
    }

    fun appendItem(item: T) {

        this.items.add(item)
        notifyItemInserted(itemCount)
    }

    fun appendItemPosition(item: T, position: Int) {
        if (this.items.isEmpty() || position < 0 || position > this.items.size) {
            return
        }
        this.items.add(position, item)
        notifyItemInserted(position)
    }

    fun updateItem(position: Int, item: T) {
        if (this.items.size > position) {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    fun removeAtPosition(position: Int) {
        if (position >= 0 && this.items.size > position) {
            items.removeAt(position)
            notifyItemRangeRemoved(position, 1)
        }
    }

    fun removeItem(item: T) {
        val position = items.indexOf(item)
        if (position > -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun appendItems(items: List<T>) {
        if (this.items.isEmpty()) {
            setData(items)
        } else {
            this.items.addAll(items)
            notifyItemRangeInserted(itemCount, items.size)
        }
    }

    fun insertItems(position: Int, items: List<T>) {
        if (this.items.isEmpty()) {
            setData(items)
        } else {
            this.items.addAll(position, items)
            notifyItemRangeInserted(position, items.size)
        }
    }

    private fun autoNotify(old: List<T>, new: List<T>) {

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = old[oldItemPosition]
                val newItem = new[newItemPosition]
                return getDiffItem().areItemsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = old[oldItemPosition]
                val newItem = new[newItemPosition]
                return getDiffItem().areContentsTheSame(oldItem, newItem)
            }

            override fun getOldListSize() = old.size

            override fun getNewListSize() = new.size
        })

        items.clear()
        items.addAll(new)

        diff.dispatchUpdatesTo(this)
    }

}