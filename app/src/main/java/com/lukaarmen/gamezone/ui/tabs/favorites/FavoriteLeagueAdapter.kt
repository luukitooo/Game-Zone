package com.lukaarmen.gamezone.ui.tabs.favorites

import com.bumptech.glide.Glide
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.adapter.BaseAdapter
import com.lukaarmen.gamezone.common.util.GameType
import com.lukaarmen.gamezone.databinding.ItemFavoriteLeagueBinding
import com.lukaarmen.gamezone.model.FavoriteLeague

class FavoriteLeagueAdapter :
    BaseAdapter<FavoriteLeague, ItemFavoriteLeagueBinding>(ItemFavoriteLeagueBinding::inflate) {

    var onItemClickListener: ((FavoriteLeague) -> Unit)? = null

    override fun onBind(binding: ItemFavoriteLeagueBinding, position: Int) {
        val favoriteLeague = getItem(position)
        binding.apply {
            tvTitle.text = favoriteLeague.title
            ivGame.setImageResource(getGameIconByType(favoriteLeague.gameType))
            Glide.with(binding.root)
                .load(favoriteLeague.imageUrl)
                .placeholder(R.drawable.ic_no_image)
                .into(ivLeague)
            root.setOnClickListener {
                onItemClickListener?.invoke(favoriteLeague)
            }
        }
    }

    private fun getGameIconByType(gameType: String): Int {
        return when (gameType) {
            GameType.CSGO.title -> GameType.CSGO.image
            GameType.DOTA2.title -> GameType.DOTA2.image
            GameType.OWERWATCH.title -> GameType.OWERWATCH.image
            else -> GameType.RAINBOW_SIX.image
        }
    }

}