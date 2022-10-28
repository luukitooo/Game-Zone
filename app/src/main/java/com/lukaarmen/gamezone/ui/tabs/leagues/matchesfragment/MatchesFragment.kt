package com.lukaarmen.gamezone.ui.tabs.leagues.matchesfragment

import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.hide
import com.lukaarmen.gamezone.common.extentions.show
import com.lukaarmen.gamezone.common.utils.TimeFrame
import com.lukaarmen.gamezone.common.utils.ViewState
import com.lukaarmen.gamezone.databinding.FragmentMatchesBinding
import com.lukaarmen.gamezone.models.Match
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MatchesFragment : BaseFragment<FragmentMatchesBinding>(FragmentMatchesBinding::inflate) {

    private val viewModel: MatchesViewModel by viewModels()

    private val args: MatchesFragmentArgs by navArgs()

    private val matchAdapter = MatchAdapter()

    private var currentTimeFrame = TimeFrame.PAST.timeFrame
    private var isSearching = false

    private var searchingJob: Job? = null

    override fun init() {
        binding.rvMatches.adapter = matchAdapter
    }

    override fun listeners(): Unit = with(binding) {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        radioGroup.setOnCheckedChangeListener { _, buttonId ->
            handleRadioButtonClick(buttonId)
        }
        btnSearch.setOnClickListener {
            isSearching = !isSearching
            setSearching(isSearching)
        }
        etSearch.doOnTextChanged { text, start, before, _ ->
            if (start != 0 || before != 0)
                searchFor(text.toString())
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.matchesFlow.collect { state ->
                handleState(state)
            }
        }
    }

    private fun handleState(state: ViewState<List<Match>>) {
        state.data?.let { matches ->
            binding.progressBar.hide()
            matchAdapter.submitList(matches.filter { it.opponents?.isNotEmpty() ?: false })
            if (matches.isEmpty())
                binding.layoutError.show()
            else
                binding.layoutError.hide()
        }
        state.error?.let { error ->
            binding.progressBar.hide()
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
        }
        state.isLoading?.let { isLoading ->
            binding.layoutError.hide()
            binding.progressBar.show()
        }
    }

    private fun handleRadioButtonClick(buttonId: Int) {
        matchAdapter.submitList(emptyList())
        binding.layoutError.hide()
        when (buttonId) {
            R.id.btnPast -> {
                currentTimeFrame = TimeFrame.PAST.timeFrame
                doInBackground {
                    viewModel.getMatchesByLeagueId(
                        leagueId = args.leagueId,
                        gameType = args.gameType,
                        timeFrame = TimeFrame.PAST.timeFrame,
                        title = binding.etSearch.text.toString()
                    )
                }
            }
            R.id.btnRunning -> {
                currentTimeFrame = TimeFrame.RUNNING.timeFrame
                doInBackground {
                    viewModel.getMatchesByLeagueId(
                        leagueId = args.leagueId,
                        gameType = args.gameType,
                        timeFrame = TimeFrame.RUNNING.timeFrame,
                        title = binding.etSearch.text.toString()
                    )
                }
            }
            R.id.btnUpcoming -> {
                currentTimeFrame = TimeFrame.UPCOMING.timeFrame
                doInBackground {
                    viewModel.getMatchesByLeagueId(
                        leagueId = args.leagueId,
                        gameType = args.gameType,
                        timeFrame = TimeFrame.UPCOMING.timeFrame,
                        title = binding.etSearch.text.toString()
                    )
                }
            }
        }
    }

    private fun setSearching(isSearching: Boolean) {
        if (isSearching) {
            binding.btnSearch.setImageResource(R.drawable.ic_cross)
            binding.etSearch.show()
        } else {
            binding.btnSearch.setImageResource(R.drawable.ic_search)
            binding.etSearch.hide()
        }
    }

    private fun searchFor(title: String) {
        searchingJob?.cancel()
        searchingJob = doInBackground {
            delay(500L)
            matchAdapter.submitList(emptyList())
            viewModel.getMatchesByLeagueId(
                leagueId = args.leagueId,
                gameType = args.gameType,
                timeFrame = currentTimeFrame,
                title = title
            )
        }
    }

}