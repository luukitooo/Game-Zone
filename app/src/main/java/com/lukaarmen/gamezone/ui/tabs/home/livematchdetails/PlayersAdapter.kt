package com.lukaarmen.gamezone.ui.tabs.home.livematchdetails

import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseAdapter
import com.lukaarmen.gamezone.common.extentions.checkPlayerName
import com.lukaarmen.gamezone.common.extentions.setPhotoByUrl
import com.lukaarmen.gamezone.databinding.ItemPlayersBinding
import com.lukaarmen.gamezone.models.MatchPlayers

class PlayersAdapter : BaseAdapter<MatchPlayers, ItemPlayersBinding>(ItemPlayersBinding::inflate) {
    override fun onBind(binding: ItemPlayersBinding, position: Int) = with(binding) {
        val item = getItem(position).inner

        ivPlayerFirst.setPhotoByUrl(item.firstTeamPlayer?.imageUrl, firstTeamProgressBar, R.drawable.img_tabata)
        ivPlayerSecond.setPhotoByUrl(item.secondTeamPlayer?.imageUrl, secondTeamProgressBar, R.drawable.img_tabata)

        tvPlayerFirstUsername.checkPlayerName(item.firstTeamPlayer?.name)
        tvPlayerSecondUsername.checkPlayerName(item.secondTeamPlayer?.name)
    }
}