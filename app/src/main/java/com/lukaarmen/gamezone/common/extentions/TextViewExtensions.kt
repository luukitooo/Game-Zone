package com.lukaarmen.gamezone.common.extentions

import androidx.appcompat.widget.AppCompatTextView

fun AppCompatTextView.checkPlayerName(name: String?) = if(name == null) this.text = "Player" else this.text = name