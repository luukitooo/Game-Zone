package com.lukaarmen.gamezone.common.extentions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.utils.Quality
import com.lukaarmen.gamezone.models.Match


fun ImageView.setLivePreview(preview: List<Match.Stream?>?, progressBar: ProgressBar?){
    if(preview != null && preview.isNotEmpty() && preview.last()?.embedUrl != null){
        Glide.with(this.context)
            .load(preview.last()?.embedUrl?.getStreamPreview(Quality.LOW))
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar?.let { it.isVisible = false }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar?.let { it.isVisible = false }
                    return false
                }
            })
            .into(this)
    }else{
        this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.img_stream_error))
        progressBar?.let { it.isVisible = false }
    }
}