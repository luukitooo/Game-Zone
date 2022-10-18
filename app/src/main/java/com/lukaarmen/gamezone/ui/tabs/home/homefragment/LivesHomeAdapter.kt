package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import com.bumptech.glide.Glide
import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.common.extentions.getStreamPreview
import com.lukaarmen.gamezone.databinding.ItemLiveHomeBinding
import com.lukaarmen.gamezone.models.Match

class LivesHomeAdapter: BaseAdapter<Match, ItemLiveHomeBinding>(ItemLiveHomeBinding::inflate) {

    override fun onBind(binding: ItemLiveHomeBinding, position: Int) = with(binding) {
        val item = getItem(position).inner

        Glide.with(this.root)
            .load(item.streamsList?.last()?.embedUrl?.getStreamPreview())
            .into(imgPreview)

        tvTitle.text = item.name
        tvLeague.text = item.matchType
    }
}