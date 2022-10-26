package com.lukaarmen.gamezone.ui.profile.profilefragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lukaarmen.gamezone.databinding.ItemSettingsGeneralBinding

class InnerAdapter : ListAdapter<SettingModel, InnerAdapter.InnerViewHolder>(InnerCallBack()) {

    var onClickListener: ((SettingType) -> Unit)? = null

    inner class InnerViewHolder(private val binding: ItemSettingsGeneralBinding) :
        ViewHolder(binding.root) {
        fun bind(item: SettingModel) {
            binding.title.text = item.title
            binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(binding.root.context, item.icon))
            binding.root.setOnClickListener { onClickListener?.invoke(item.type)
                Log.d("myLog_inner_adapter", item.type.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        return InnerViewHolder(
            ItemSettingsGeneralBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class InnerCallBack : DiffUtil.ItemCallback<SettingModel>() {
        override fun areItemsTheSame(oldItem: SettingModel, newItem: SettingModel) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: SettingModel, newItem: SettingModel) =
            oldItem.title == newItem.title
    }


}