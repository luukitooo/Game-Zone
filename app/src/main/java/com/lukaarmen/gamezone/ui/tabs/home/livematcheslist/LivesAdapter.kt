package com.lukaarmen.gamezone.ui.tabs.home.livematcheslist

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.common.extentions.getStreamPreview
import com.lukaarmen.gamezone.common.utils.Quality
import com.lukaarmen.gamezone.databinding.ItemLiveBinding
import com.lukaarmen.gamezone.models.Match

class LivesAdapter : BaseAdapter<Match, ItemLiveBinding>(ItemLiveBinding::inflate) {

    override fun onBind(binding: ItemLiveBinding, position: Int) = with(binding) {
        val item = getItem(position).inner

        if (item.streamsList != null && item.streamsList.isNotEmpty()) {
            Glide.with(this.root)
                .load(item.streamsList.last()?.embedUrl?.getStreamPreview(Quality.LOW))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                })
                .into(imgPreview)

        } else {
            imgPreview.setImageDrawable(
                ContextCompat.getDrawable(
                    imgPreview.context,
                    R.drawable.img_stream_error
                )
            )
            progressBar.isVisible = false
        }

        tvTitle.text = item.name
        tvLeague.text = item.beginAt
    }
}