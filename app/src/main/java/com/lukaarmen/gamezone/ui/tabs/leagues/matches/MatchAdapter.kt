package com.lukaarmen.gamezone.ui.tabs.leagues.matches

import android.graphics.drawable.Drawable
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.adapter.BaseAdapter
import com.lukaarmen.gamezone.databinding.ItemMatchBinding
import com.lukaarmen.gamezone.model.Match

class MatchAdapter: BaseAdapter<Match, ItemMatchBinding>(ItemMatchBinding::inflate) {

    var onClickListener: ((Int) -> Unit)? = null

    override fun onBind(binding: ItemMatchBinding, position: Int) {
        val match = getItem(position)
        binding.apply {
            tvFirstTeamName.text = try {
                match.opponents!![0]?.name
            } catch (t: Throwable) {
                "No Name..."
            }
            tvSecondTeamName.text = try {
                match.opponents!![1]?.name
            } catch (t: Throwable) {
                "No Name..."
            }
            tvFirstTeamScore.text = try {
                match.results!![0]?.score.toString()
            } catch (t: Throwable) { "0" }
            tvSecondTeamScore.text = try {
                match.results!![1]?.score.toString()
            } catch (t: Throwable) { "0" }
            Glide.with(tmgFirstTeamLogo)
                .load(try {
                    match.opponents!![0]?.imageUrl
                } catch (t: Throwable) { "" })
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        tmgFirstTeamLogo.isVisible = true
                        pbFirst.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        tmgFirstTeamLogo.isVisible = true
                        pbFirst.isVisible = false
                        return false
                    }
                })
                .placeholder(R.drawable.ic_no_image)
                .into(tmgFirstTeamLogo)
            Glide.with(tmgSecondTeamLogo)
                .load(try {
                    match.opponents!![1]?.imageUrl
                } catch (t: Throwable) { "" })
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        tmgSecondTeamLogo.isVisible = true
                        pbSecond.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        tmgSecondTeamLogo.isVisible = true
                        pbSecond.isVisible = false
                        return false
                    }
                })
                .placeholder(R.drawable.ic_no_image)
                .into(tmgSecondTeamLogo)

            root.setOnClickListener {
                match.id?.let { it1 -> onClickListener?.invoke(it1) }
            }
        }
    }

}