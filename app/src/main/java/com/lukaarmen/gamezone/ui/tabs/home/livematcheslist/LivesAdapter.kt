package com.lukaarmen.gamezone.ui.tabs.home.livematcheslist

import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.common.extentions.filterDate
import com.lukaarmen.gamezone.common.extentions.setLivePreview
import com.lukaarmen.gamezone.databinding.ItemLiveBinding
import com.lukaarmen.gamezone.models.Match

class LivesAdapter : BaseAdapter<Match, ItemLiveBinding>(ItemLiveBinding::inflate) {

    var onClickListener: ((Int) -> Unit)? = null

    override fun onBind(binding: ItemLiveBinding, position: Int) = with(binding) {
        val item = getItem(position).inner

        imgPreview.setLivePreview(item.streamsList, progressBar)

        tvTitle.text = item.name
        tvLeague.text = item.beginAt?.filterDate()

        root.setOnClickListener { onClickListener?.invoke(item.id!!) }
    }
}