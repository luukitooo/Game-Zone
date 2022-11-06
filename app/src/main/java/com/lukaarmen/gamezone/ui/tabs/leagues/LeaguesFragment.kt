package com.lukaarmen.gamezone.ui.tabs.leagues

import android.util.Log.d
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extension.doInBackground
import com.lukaarmen.gamezone.common.extension.hide
import com.lukaarmen.gamezone.common.extension.show
import com.lukaarmen.gamezone.common.util.ViewState
import com.lukaarmen.gamezone.databinding.FragmentLeaguesBinding
import com.lukaarmen.gamezone.model.FavoriteLeague
import com.lukaarmen.gamezone.model.League
import com.lukaarmen.gamezone.ui.tabs.home.GamesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class LeaguesFragment : BaseFragment<FragmentLeaguesBinding>(FragmentLeaguesBinding::inflate) {

    private val viewModel: LeaguesViewModel by viewModels()

    @Inject
    lateinit var auth: FirebaseAuth

    private val leagueAdapter = LeagueAdapter()
    private val gamesAdapter = GamesAdapter()

    private var isSearching = false
    private var searchJob: Job? = null
    private var saveSnackBar: Snackbar? = null

    override fun init(): Unit = with(binding) {
        rvLeagues.adapter = leagueAdapter
        rvGames.adapter = gamesAdapter
        doInBackground {
            viewModel.getLeagues()
        }
    }

    override fun listeners() {
        gamesAdapter.onClickListener = { gameIndicator ->
            doInBackground {
                leagueAdapter.submitList(emptyList())
                viewModel.getLeagues(
                    name = binding.etSearch.text.toString(),
                    gameType = gameIndicator.title
                )
                viewModel.updateIndicators(gameIndicator)
            }
        }
        leagueAdapter.onItemClickListener = { league ->
            leagueAdapter.submitList(emptyList())
            findNavController().navigate(
                LeaguesFragmentDirections.actionLeaguesFragmentToMatchesFragment(
                    leagueId = league.id!!,
                    gameType = gamesAdapter.currentList.find { it.isSelected }!!.gameType.title
                )
            )
        }
        leagueAdapter.onItemLongClickListener = { league ->
            doInBackground {
                viewModel.checkLeagueSaved(
                    league.toFavoriteLeague(
                        uid = auth.currentUser!!.uid,
                        gameType = gamesAdapter.currentList.find { it.isSelected }!!.gameType.title
                    )
                )
            }
        }
        binding.btnSearch.setOnClickListener {
            isSearching = !isSearching
            setSearching(isSearching)
        }
        binding.etSearch.doOnTextChanged { leagueTitle, start, before, _ ->
            viewModel.setSearchQuery(leagueTitle.toString())
            if (start != 0 || before != 0) {
                doInBackground {
                    searchFor(leagueTitle.toString())
                }
            }
        }
        binding.swipeToRefreshLayout.setOnRefreshListener {
            doInBackground {
                leagueAdapter.submitList(emptyList())
                viewModel.getLeagues(withLoader = false)
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
        doInBackground {
            viewModel.isLeagueAlreadySavedFlow.collect { favoriteLeague ->
                handleSavedLeagueCheck(favoriteLeague)
            }
        }
    }

    private fun handleSavedLeagueCheck(league: FavoriteLeague?) {
        league?.let {
            saveSnackBar = Snackbar.make(
                binding.root,
                getString(R.string.save_confirmation),
                Snackbar.LENGTH_LONG
            )
                .setAnchorView(binding.glLayoutBottom)
                .setAction(getString(R.string.yes)) {
                    doInBackground {
                        viewModel.addLeagueToFavorites(league)
                        viewModel.getLeagues(withLoader = false)
                    }
                }
            saveSnackBar?.show()
        } ?: run {
            Snackbar.make(binding.root, getString(R.string.already_saved), Snackbar.LENGTH_SHORT)
                .setAnchorView(binding.glLayoutBottom)
                .show()
        }
    }

    private fun handleState(state: ViewState<List<League>>) {
        state.apply {
            data?.let { leagues ->
                leagueAdapter.submitList(leagues).also {
                    binding.rvLeagues.startLayoutAnimation()
                }
                binding.progressBar.isVisible = false
                binding.swipeToRefreshLayout.isRefreshing = false
            }
            error?.let { errorMessage ->
                Snackbar.make(binding.root, getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG)
                    .setAnchorView(binding.glLayoutBottom)
                    .show()
                binding.progressBar.isVisible = false
            }
            isLoading?.let { isLoading ->
                binding.progressBar.isVisible = true
            }
        }
    }

    private fun setSearching(isSearching: Boolean): Unit = with(binding) {
        if (isSearching) {
            btnSearch.setImageResource(R.drawable.ic_cross)
            etSearch.show()
        } else {
            btnSearch.setImageResource(R.drawable.ic_search)
            etSearch.hide()
        }
    }

    private fun searchFor(leagueTitle: String) {
        searchJob?.cancel()
        searchJob = doInBackground {
            delay(500L)
            leagueAdapter.submitList(emptyList())
            viewModel.getLeagues(
                name = leagueTitle
            )
        }
    }

    override fun onPause() {
        super.onPause()
        saveSnackBar?.dismiss()
    }

}