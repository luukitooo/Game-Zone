package com.lukaarmen.gamezone.ui.tabs.home.livematchdetails

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.filterDate
import com.lukaarmen.gamezone.common.extentions.setPlayerPhoto
import com.lukaarmen.gamezone.databinding.FragmentLiveMatchDetailsBinding
import com.lukaarmen.gamezone.models.Match
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LiveMatchDetailsFragment : BaseFragment<FragmentLiveMatchDetailsBinding>(
    FragmentLiveMatchDetailsBinding::inflate
) {

    private val viewModel by viewModels<LiveMatchDetailsViewModel>()
    private val playersAdapter: PlayersAdapter by lazy { PlayersAdapter() }
    override fun init() {
        return
    }

    override fun listeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.matchState.collect {
                it.data?.let { match -> successState(match) }
                it.error?.let { error -> errorState(error) }
                it.isLoading?.let { loadingState() }
            }
        }

        doInBackground {
            viewModel.matchPlayers.collect {
                playersAdapter.submitList(it)
            }
        }

    }

    private fun successState(match: Match) = with(binding) {
        initRecyclerView()
        progressBar.isVisible = false

        ivTeamFirst.setPlayerPhoto(match.opponents?.first()?.imageUrl, R.drawable.img_tabata)
        ivTeamSecond.setPlayerPhoto(match.opponents?.last()?.imageUrl, R.drawable.img_tabata)

        tvScoreFirst.text = match.results?.first()?.score.toString()
        tvScoreSecond.text = match.results?.last()?.score.toString()

        tvTitle.text = match.name
        tvStartTime.text = match.beginAt.toString().filterDate()

    }

    private fun errorState(error: String) = with(binding) {
        progressBar.isVisible = false
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    private fun loadingState() = with(binding) {
        progressBar.isVisible = true
    }

    private fun initRecyclerView() = with(binding.rvPlayers) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = playersAdapter
    }


}