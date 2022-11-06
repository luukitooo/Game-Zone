package com.lukaarmen.gamezone.ui.profile.adapter

import androidx.core.content.ContextCompat
import com.lukaarmen.gamezone.common.base.adapter.BaseAdapter
import com.lukaarmen.gamezone.common.extension.hide
import com.lukaarmen.gamezone.common.extension.show
import com.lukaarmen.gamezone.databinding.ItemSettingBinding
import com.lukaarmen.gamezone.ui.profile.settings.SettingsGroup
import com.lukaarmen.gamezone.ui.profile.settings.SettingsType


class SettingItemAdapter :
    BaseAdapter<SettingsGroup.SettingItem, ItemSettingBinding>(ItemSettingBinding::inflate) {

    var onClickListenerInner: ((SettingsType) -> Unit)? = null

    override fun onBind(binding: ItemSettingBinding, position: Int) {
        val item = getItem(position).inner

        binding.title.text = item.title
        binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(binding.root.context, item.icon))
        binding.root.setOnClickListener { onClickListenerInner?.invoke(item.type) }

        when (item.type) {
            SettingsType.USERNAME, SettingsType.PASSWORD, SettingsType.PHOTO -> {
                binding.notificationsSwitch.hide()
                binding.secondIcon.show()
            }
            else -> {
                binding.notificationsSwitch.hide()
                binding.secondIcon.hide()
            }
        }
    }
}