package com.lukaarmen.gamezone.common.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.lukaarmen.gamezone.common.base.Inflater

abstract class BaseAdapter<T : Recyclable<T>, VB : ViewBinding>(private val inflater: Inflater<VB>) :
    ListAdapter<T, BaseAdapter<T, VB>.BaseViewHolder>(BaseItemCallback<T>()) {

    abstract fun onBind(binding: VB, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder(
        inflater.invoke(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind()
    }

    inner class BaseViewHolder(private val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            onBind(binding, adapterPosition)
        }
    }

    private class BaseItemCallback<T : Recyclable<T>> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.uniqueValue == newItem.uniqueValue
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

    }
}