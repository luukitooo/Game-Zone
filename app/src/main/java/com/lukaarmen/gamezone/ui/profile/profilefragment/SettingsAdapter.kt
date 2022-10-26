package com.lukaarmen.gamezone.ui.profile.profilefragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lukaarmen.gamezone.databinding.ItemSettingsBinding

class SettingsAdapter :
    ListAdapter<SettingsModel2, SettingsAdapter.SettingsViewHolder>(SettingsCallBack()) {

    var onClickListener: ((SettingType) -> Unit)? = null

    inner class SettingsViewHolder(private val binding: ItemSettingsBinding) :
        ViewHolder(binding.root) {
        fun bind(item: SettingsModel2) {
            val innerAdapter: InnerAdapter by lazy { InnerAdapter() }
            binding.title.text = item.title
            binding.innerRv.layoutManager = LinearLayoutManager(binding.innerRv.context)
            binding.innerRv.adapter = innerAdapter
            innerAdapter.submitList(item.list)
            binding.root.setOnClickListener {
                innerAdapter.onClickListener = {
                    Log.d("myLog_adapter", it.toString())
                    onClickListener?.invoke(it)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        return SettingsViewHolder(
            ItemSettingsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class SettingsCallBack : DiffUtil.ItemCallback<SettingsModel2>() {
        override fun areItemsTheSame(oldItem: SettingsModel2, newItem: SettingsModel2) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: SettingsModel2, newItem: SettingsModel2) =
            oldItem == newItem

    }
}