package com.lukaarmen.gamezone.ui.tabs.home.live_matches_list.match_details

import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.adapter.BaseAdapter
import com.lukaarmen.gamezone.common.extension.checkPlayerName
import com.lukaarmen.gamezone.common.extension.setPhotoByUrl
import com.lukaarmen.gamezone.databinding.ItemPlayersBinding
import com.lukaarmen.gamezone.model.MatchPlayers

class PlayersAdapter : BaseAdapter<MatchPlayers, ItemPlayersBinding>(ItemPlayersBinding::inflate) {
    override fun onBind(binding: ItemPlayersBinding, position: Int) = with(binding) {
        val item = getItem(position).inner

        ivPlayerFirst.setPhotoByUrl(item.firstTeamPlayer?.imageUrl, firstTeamProgressBar, R.drawable.img_player)
        ivPlayerSecond.setPhotoByUrl(item.secondTeamPlayer?.imageUrl, secondTeamProgressBar, R.drawable.img_player)

        tvPlayerFirstUsername.checkPlayerName(item.firstTeamPlayer?.name)
        tvPlayerSecondUsername.checkPlayerName(item.secondTeamPlayer?.name)
    }
}