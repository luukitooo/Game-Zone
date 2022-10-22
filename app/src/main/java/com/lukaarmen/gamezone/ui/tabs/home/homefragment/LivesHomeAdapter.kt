package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.common.extentions.filterDate
import com.lukaarmen.gamezone.common.extentions.setLivePreview
import com.lukaarmen.gamezone.databinding.ItemLiveHomeBinding
import com.lukaarmen.gamezone.models.Match

class LivesHomeAdapter : BaseAdapter<Match, ItemLiveHomeBinding>(ItemLiveHomeBinding::inflate) {

    var onCLickListener: ((Int) -> Unit)? = null

    override fun onBind(binding: ItemLiveHomeBinding, position: Int) = with(binding) {
        val item = getItem(position).inner

        imgPreview.setLivePreview(item.streamsList, progressBar)
        tvTitle.text = item.name
        tvLeague.text = item.beginAt?.filterDate()

        root.setOnClickListener { onCLickListener?.invoke(item.id!!) }
    }
}