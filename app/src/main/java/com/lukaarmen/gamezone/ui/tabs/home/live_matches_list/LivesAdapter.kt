package com.lukaarmen.gamezone.ui.tabs.home.live_matches_list

import android.util.Log.d
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.adapter.BaseAdapter
import com.lukaarmen.gamezone.common.extension.checkForPreview
import com.lukaarmen.gamezone.common.extension.filterDate
import com.lukaarmen.gamezone.common.extension.getStreamPreview
import com.lukaarmen.gamezone.common.extension.setPhotoByUrl
import com.lukaarmen.gamezone.common.util.Quality
import com.lukaarmen.gamezone.databinding.ItemLiveBinding
import com.lukaarmen.gamezone.model.Match

class LivesAdapter : BaseAdapter<Match, ItemLiveBinding>(ItemLiveBinding::inflate) {

    var onClickListener: ((Int) -> Unit)? = null

    override fun onBind(binding: ItemLiveBinding, position: Int) = with(binding) {
        val item = getItem(position).inner

        item.streamsList

        imgPreview.setPhotoByUrl(
            url = item.streamsList?.checkForPreview(),
            progressBar = progressBar,
            placeHolder = R.drawable.img_stream_error
        )

        d("ksjdv", item.streamsList?.checkForPreview().toString())

        tvTitle.text = item.name
        tvLeague.text = item.beginAt?.filterDate()

        root.setOnClickListener { item.id?.let { it1 -> onClickListener?.invoke(it1) } }
    }
}