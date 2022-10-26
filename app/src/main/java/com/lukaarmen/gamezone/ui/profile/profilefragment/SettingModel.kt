package com.lukaarmen.gamezone.ui.profile.profilefragment

import com.lukaarmen.gamezone.R

enum class SettingType(){
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
    val title: String
)

data class SettingsModel2(
    val title: String,
    val list: List<SettingModel>
)

val generalSettings = mutableListOf(
    SettingModel(SettingType.USERNAME, R.drawable.ic_user,"Username"),
    SettingModel(SettingType.PASSWORD, R.drawable.ic_lock,"Password"),
    SettingModel(SettingType.PHOTO, R.drawable.ic_photo,"Photo")
)
val otherSettings = mutableListOf(
    SettingModel(SettingType.NOTIFICATIONS, R.drawable.ic_notification,"Notifications"),
    SettingModel(SettingType.SIGN_OUT, R.drawable.ic_signout,"Sign out")
)

val information = mutableListOf(
    SettingModel(SettingType.HELP, R.drawable.ic_help,"Help"),
    SettingModel(SettingType.ABOUT_US, R.drawable.ic_info,"About us")
)

val settings = listOf(
    SettingsModel2("Edit profile", generalSettings),
    SettingsModel2("Other", otherSettings),
    SettingsModel2("Info", information)

)
