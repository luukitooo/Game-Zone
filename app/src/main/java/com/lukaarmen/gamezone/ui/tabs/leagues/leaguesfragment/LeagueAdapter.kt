package com.lukaarmen.gamezone.ui.tabs.leagues.leaguesfragment

import com.bumptech.glide.Glide
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.databinding.ItemLeagueBinding
import com.lukaarmen.gamezone.models.League

class LeagueAdapter : BaseAdapter<League, ItemLeagueBinding>(ItemLeagueBinding::inflate) {

    var onItemClickListener: ((League) -> Unit)? = null

    override fun onBind(binding: ItemLeagueBinding, position: Int) {
        val league = getItem(position).inner
        Glide.with(binding.ivLeague)
            .load(league.imageUrl)
            .placeholder(R.drawable.ic_error)
            .into(binding.ivLeague)
        binding.apply {
            tvTitle.text = league.name
            root.setOnClickListener {
                onItemClickListener?.invoke(league)
            }
        }
    }

}