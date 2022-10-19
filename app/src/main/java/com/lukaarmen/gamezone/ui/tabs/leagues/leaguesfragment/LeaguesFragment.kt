package com.lukaarmen.gamezone.ui.tabs.leagues.leaguesfragment

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.utils.CategoryIndicator
import com.lukaarmen.gamezone.common.utils.GameType
import com.lukaarmen.gamezone.common.utils.ViewState
import com.lukaarmen.gamezone.databinding.FragmentLeaguesBinding
import com.lukaarmen.gamezone.models.League
import com.lukaarmen.gamezone.ui.tabs.home.homefragment.GamesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LeaguesFragment : BaseFragment<FragmentLeaguesBinding>(FragmentLeaguesBinding::inflate) {

    private val viewModel: LeaguesViewModel by viewModels()

    private val leagueAdapter = LeagueAdapter()
    private val gamesAdapter = GamesAdapter()

    override fun init() = with(binding) {
        rvLeagues.adapter = leagueAdapter
        rvGames.adapter = gamesAdapter
    }

    override fun listeners() {
        gamesAdapter.onClickListener = { gameIndicator ->
            doInBackground {
                leagueAdapter.submitList(emptyList())
                viewModel.getLeagues(gameIndicator.title)
                viewModel.updateIndicators(gameIndicator)
            }
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.leaguesFlow.collect { state ->
                handleState(state)
            }
        }
        doInBackground {
            viewModel.indicatorsFlow.collect { updatedIndicators ->
                gamesAdapter.submitList(updatedIndicators)
            }
        }
    }

    private fun handleState(state: ViewState<List<League>>) {
        state.apply {
            data?.let { leagues ->
                leagueAdapter.submitList(leagues)
                binding.progressBar.isVisible = false
            }
            error?.let { errorMessage ->
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                binding.progressBar.isVisible = false
            }
            isLoading?.let { isLoading ->
                binding.progressBar.isVisible = true
            }
        }
    }

}