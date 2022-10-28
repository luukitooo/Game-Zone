package com.lukaarmen.gamezone.ui.profile.profilefragment.adapters

import androidx.recyclerview.widget.LinearLayoutManager
import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.databinding.ItemSettingsGroupBinding
import com.lukaarmen.gamezone.ui.profile.profilefragment.settings.SettingsGroup
import com.lukaarmen.gamezone.ui.profile.profilefragment.settings.SettingsType

class SettingsAdapter :
    BaseAdapter<SettingsGroup, ItemSettingsGroupBinding>(ItemSettingsGroupBinding::inflate) {

    var onClickListener: ((SettingsType) -> Unit)? = null
    var notificationListener: ((Boolean) -> Unit)? = null

    override fun onBind(binding: ItemSettingsGroupBinding, position: Int) {
        val item = getItem(position).inner

        val innerAdapter: InnerAdapter by lazy { InnerAdapter() }
        binding.title.text = item.title
        binding.innerRv.layoutManager = LinearLayoutManager(binding.innerRv.context)
        binding.innerRv.adapter = innerAdapter
        innerAdapter.submitList(item.list)

        innerAdapter.notificationListenerInner = {
            notificationListener?.invoke(it)
        }
        innerAdapter.onClickListenerInner = {
            onClickListener?.invoke(it)
        }
    }
}