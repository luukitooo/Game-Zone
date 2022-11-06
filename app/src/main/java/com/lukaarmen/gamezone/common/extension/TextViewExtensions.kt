package com.lukaarmen.gamezone.common.extension

import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView


fun TextView.makeLink(link: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(textPaint: TextPaint) {
            textPaint.color = textPaint.linkColor
            textPaint.isUnderlineText = false
        }

        override fun onClick(view: View) {
            Selection.setSelection((view as TextView).text as Spannable, 0)
            view.invalidate()
            link.second.onClick(view)
        }
    }
    startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
    spannableString.setSpan(
        clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun TextView.checkPlayerName(name: String?) = if(name == null) this.text = "Player" else this.text = name