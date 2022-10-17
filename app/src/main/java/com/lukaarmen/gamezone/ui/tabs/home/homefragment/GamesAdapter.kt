package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import androidx.core.content.ContextCompat
import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.common.utils.CategoryIndicator
import com.lukaarmen.gamezone.common.utils.GameType
import com.lukaarmen.gamezone.databinding.ItemGameBinding

class GamesAdapter : BaseAdapter<CategoryIndicator, ItemGameBinding>(ItemGameBinding::inflate) {

    var onClickListener: ((GameType) -> Unit)? = null

    override fun onBind(binding: ItemGameBinding, position: Int) = with(binding) {
        val item = getItem(position).inner
        root.setImageDrawable(ContextCompat.getDrawable(root.context, item.gameType.image))
        root.setOnClickListener {
            onClickListener?.invoke(item.gameType)
        }
    }
}