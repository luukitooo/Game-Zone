package com.lukaarmen.gamezone.common.extension

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

fun ImageView.setPhotoByUrl(url: String?, progressBar: ProgressBar? = null, placeHolder: Int? = null){
    if( url != "" && url != null ){
        Glide.with(this.context)
            .load(url)
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
        progressBar?.let { it.isVisible = false }
        placeHolder?.let { this.setImageDrawable(ContextCompat.getDrawable(this.context, it)) }
    }
}