package com.lukaarmen.gamezone.ui.profile.profilefragment

import com.lukaarmen.gamezone.R

enum class SettingType{
    USERNAME,
    PASSWORD,
    PHOTO,
    NOTIFICATIONS,
    SIGN_OUT,
    HELP,
    ABOUT_US
}

data class SettingModel(
    val type: SettingType,
    val icon: Int,
    val title: String,
    val isWithSwitch: Boolean
)

data class SettingsModel2(
    val title: String,
    val list: List<SettingModel>
)

val editProfile = mutableListOf(
    SettingModel(SettingType.USERNAME, R.drawable.ic_user,"Username", false),
    SettingModel(SettingType.PASSWORD, R.drawable.ic_lock,"Password", false),
    SettingModel(SettingType.PHOTO, R.drawable.ic_photo,"Photo", false)
)
val otherSettings = mutableListOf(
    SettingModel(SettingType.NOTIFICATIONS, R.drawable.ic_notification,"Notifications", true),
    SettingModel(SettingType.SIGN_OUT, R.drawable.ic_signout,"Sign out", false)
)
val information = mutableListOf(
    SettingModel(SettingType.HELP, R.drawable.ic_help,"Help", false),
    SettingModel(SettingType.ABOUT_US, R.drawable.ic_info,"About us", false)
)
val settings = listOf(
    SettingsModel2("Edit profile", editProfile),
    SettingsModel2("Other", otherSettings),
    SettingsModel2("Info", information)
)
