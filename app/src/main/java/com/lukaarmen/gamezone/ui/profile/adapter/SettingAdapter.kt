package com.lukaarmen.gamezone.ui.profile.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import com.lukaarmen.gamezone.common.base.adapter.BaseAdapter
import com.lukaarmen.gamezone.databinding.ItemSettingsGroupBinding
import com.lukaarmen.gamezone.ui.profile.settings.SettingsGroup
import com.lukaarmen.gamezone.ui.profile.settings.SettingsType

class SettingAdapter :
    BaseAdapter<SettingsGroup, ItemSettingsGroupBinding>(ItemSettingsGroupBinding::inflate) {

    var onClickListener: ((SettingsType) -> Unit)? = null

    override fun onBind(binding: ItemSettingsGroupBinding, position: Int) {
        val item = getItem(position).inner

        val settingItemAdapter: SettingItemAdapter by lazy { SettingItemAdapter() }
        binding.title.text = item.title
        binding.innerRv.layoutManager = LinearLayoutManager(binding.innerRv.context)
        binding.innerRv.adapter = settingItemAdapter
        settingItemAdapter.submitList(item.list)

        settingItemAdapter.onClickListenerInner = {
            onClickListener?.invoke(it)
        }
    }
}