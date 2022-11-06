package com.lukaarmen.gamezone.ui.tabs.home

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.adapter.BaseAdapter
import com.lukaarmen.gamezone.model.CategoryIndicator
import com.lukaarmen.gamezone.common.util.GameType
import com.lukaarmen.gamezone.databinding.ItemGameBinding

class GamesAdapter : BaseAdapter<CategoryIndicator, ItemGameBinding>(ItemGameBinding::inflate) {

    var onClickListener: ((GameType) -> Unit)? = null

    override fun onBind(binding: ItemGameBinding, position: Int) = with(binding) {
        val item = getItem(position).inner

        when (item.isSelected) {
            true -> {
                root.isEnabled = false
                root.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.app_yellow))
            }
            false -> {
                root.isEnabled = true
                root.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(root.context, R.color.app_background_light)
                )
            }
        }

        root.setImageDrawable(ContextCompat.getDrawable(root.context, item.gameType.image))
        root.setOnClickListener {
            onClickListener?.invoke(item.gameType)
        }
    }
}