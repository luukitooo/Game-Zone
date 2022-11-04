package com.lukaarmen.gamezone.ui.tabs.leagues.leaguesfragment

import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.hide
import com.lukaarmen.gamezone.common.extentions.show
import com.lukaarmen.gamezone.common.utils.ViewState
import com.lukaarmen.gamezone.databinding.FragmentLeaguesBinding
import com.lukaarmen.gamezone.models.FavoriteLeague
import com.lukaarmen.gamezone.models.League
import com.lukaarmen.gamezone.ui.tabs.home.homefragment.GamesAdapter
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

    override fun init() = with(binding) {
        rvLeagues.adapter = leagueAdapter
        rvGames.adapter = gamesAdapter
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
            if (start != 0 || before != 0) {
                doInBackground {
                    searchFor(leagueTitle.toString())
                }
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
                "You really want to save this league?",
                Snackbar.LENGTH_LONG
            )
                .setAnchorView(binding.glLayoutBottom)
                .setAction("Yes") {
                    doInBackground {
                        viewModel.addLeagueToFavorites(league)
                    }
                }
            saveSnackBar?.show()
        } ?: run {
            Snackbar.make(binding.root, "League is already saved...", Snackbar.LENGTH_SHORT)
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
                gameType = gamesAdapter.currentList.find { it.isSelected }!!.gameType.title,
                name = leagueTitle
            )
        }
    }

    override fun onPause() {
        super.onPause()
        saveSnackBar?.dismiss()
    }

}