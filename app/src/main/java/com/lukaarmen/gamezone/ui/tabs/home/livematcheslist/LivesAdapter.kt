package com.lukaarmen.gamezone.ui.tabs.home.livematcheslist

import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.common.extentions.setLivePreview
import com.lukaarmen.gamezone.databinding.ItemLiveBinding
import com.lukaarmen.gamezone.models.Match

class LivesAdapter : BaseAdapter<Match, ItemLiveBinding>(ItemLiveBinding::inflate) {

    override fun onBind(binding: ItemLiveBinding, position: Int) = with(binding) {
        val item = getItem(position).inner

        imgPreview.setLivePreview(item.streamsList, progressBar)

        tvTitle.text = item.name
        tvLeague.text = item.beginAt
    }
}