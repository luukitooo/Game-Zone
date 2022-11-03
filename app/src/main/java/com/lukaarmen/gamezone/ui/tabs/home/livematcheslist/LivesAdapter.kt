package com.lukaarmen.gamezone.ui.tabs.home.livematcheslist

import android.util.Log.d
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.common.extentions.filterDate
import com.lukaarmen.gamezone.common.extentions.getStreamPreview
import com.lukaarmen.gamezone.common.extentions.setPhotoByUrl
import com.lukaarmen.gamezone.common.utils.Quality
import com.lukaarmen.gamezone.databinding.ItemLiveBinding
import com.lukaarmen.gamezone.models.Match

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


fun List<Match.Stream?>?.checkForPreview(): String? {
    this?.let {
        return if (this.isNotEmpty()) this.last()?.embedUrl?.getStreamPreview(Quality.LOW)
        else null
    }
    return null
}