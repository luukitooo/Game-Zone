package com.lukaarmen.gamezone.ui.profile.profilefragment.adapters

import androidx.core.content.ContextCompat
import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.common.extentions.hide
import com.lukaarmen.gamezone.common.extentions.show
import com.lukaarmen.gamezone.databinding.ItemSettingBinding
import com.lukaarmen.gamezone.ui.profile.profilefragment.settings.SettingsGroup
import com.lukaarmen.gamezone.ui.profile.profilefragment.settings.SettingsType


class InnerAdapter :
    BaseAdapter<SettingsGroup.SettingItem, ItemSettingBinding>(ItemSettingBinding::inflate) {

    var onClickListenerInner: ((SettingsType) -> Unit)? = null
    var notificationListenerInner: ((Boolean) -> Unit)? = null

    override fun onBind(binding: ItemSettingBinding, position: Int) {
        val item = getItem(position).inner

        binding.title.text = item.title
        binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(binding.root.context, item.icon))
        binding.root.setOnClickListener { onClickListenerInner?.invoke(item.type) }

        binding.notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            notificationListenerInner?.invoke(isChecked)
        }

        when (item.type) {
            SettingsType.NOTIFICATIONS -> {
                binding.notificationsSwitch.show()
                binding.secondIcon.hide()
            }
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