package com.lukaarmen.gamezone.ui.profile.settings

import com.lukaarmen.gamezone.common.base.adapter.Recyclable

data class SettingsGroup(
    val title: String,
    val list: List<SettingItem>
) : Recyclable<SettingsGroup>() {

    override val inner: SettingsGroup
        get() = this

    override val uniqueValue: Any
        get() = title

    override fun compareTo(other: Any?): Boolean {
        return other is SettingsGroup && this == other
    }

    data class SettingItem(
        val type: SettingsType,
        val icon: Int,
        val title: String
    ) : Recyclable<SettingItem>() {

        override val inner: SettingItem
            get() = this

        override val uniqueValue: Any
            get() = title

        override fun compareTo(other: Any?): Boolean {
            return other is SettingItem && this == other
        }
    }
}
