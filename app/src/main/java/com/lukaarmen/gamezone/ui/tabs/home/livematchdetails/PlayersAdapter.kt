package com.lukaarmen.gamezone.ui.tabs.home.livematchdetails

import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.common.extentions.setPlayerPhoto
import com.lukaarmen.gamezone.databinding.ItemPlayersBinding
import com.lukaarmen.gamezone.models.MatchPlayers

class PlayersAdapter : BaseAdapter<MatchPlayers, ItemPlayersBinding>(ItemPlayersBinding::inflate) {
    override fun onBind(binding: ItemPlayersBinding, position: Int) = with(binding) {
        val item = getItem(position).inner

        ivPlayerFirst.setPlayerPhoto(item.firstTeamPlayer?.imageUrl, R.drawable.img_tabata)
        ivPlayerSecond.setPlayerPhoto(item.secondTeamPlayer?.imageUrl, R.drawable.img_tabata)

        tvPlayerFirstUsername.text = item.firstTeamPlayer?.name
        tvPlayerSecondUsername.text = item.secondTeamPlayer?.name
    }
}