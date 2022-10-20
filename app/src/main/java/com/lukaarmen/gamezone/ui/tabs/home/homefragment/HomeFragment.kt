package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.setLivePreview
import com.lukaarmen.gamezone.common.utils.GameType
import com.lukaarmen.gamezone.databinding.FragmentHomeBinding
import com.lukaarmen.gamezone.models.Match
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    private val viewModel by viewModels<HomeViewModel>()
    private val gamesAdapter: GamesAdapter by lazy { GamesAdapter() }
    private val livesAdapter: LivesHomeAdapter by lazy { LivesHomeAdapter() }
    private var currentGameType = GameType.ALL
    private var streamsCount = 0

    override fun init() {
        initGamesRecycler()
        initLivesRecycler()
    }

    override fun listeners() {
        binding.tvShowAll.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToLiveMatchesListFragment(
                    currentGameType.title
                )
            )
        }

        gamesAdapter.onClickListener = { gameType ->
            currentGameType = gameType

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.updateGamesList(gameType)
            }
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.viewState.collect {
                it.data?.let { matchesList -> successfulState(matchesList) }
                it.error?.let { error -> errorState(error) }
                it.isLoading?.let { loadingState() }
            }
        }

        doInBackground {
            viewModel.gamesListState.collect {
                gamesAdapter.submitList(it)
            }
        }

        doInBackground {
            viewModel.streamsCountState.collect {
                streamsCount = it
            }
        }
    }

    private fun successfulState(data: List<Match>) = with(binding) {
        val list = data.take(5).toMutableList()
        when (data.size) {
            0 -> {
                latestLiveErrorState()
                tvMessage.text = requireContext().getString(R.string.no_data)
            }
            else -> {
                if (data.size != 1) list.removeFirst()
                ivNewestLive.setLivePreview(data[0].streamsList, null)
                btnPlay.visibility = View.VISIBLE
            }
        }

        when (streamsCount) {
            in 0..2 -> {
                tvShowAll.setTextColor(requireContext().getColor(R.color.app_grey_light))
                tvShowAll.isEnabled = false
            }
            else -> {
                tvShowAll.setTextColor(requireContext().getColor(R.color.app_yellow))
                tvShowAll.isEnabled = true
            }
        }

        binding.tvLivesCount.text = streamsCount.toString()
        livesAdapter.submitList(list)
        livesRecyclerProgressBar.visibility = View.GONE
        latestStreamProgressBar.visibility = View.GONE
    }

    private fun errorState(error: String) = with(binding) {
        //recyclerview
        livesAdapter.submitList(emptyList())
        livesRecyclerProgressBar.visibility = View.GONE
        tvMessage.text = requireContext().getString(R.string.error)
        //latest live
        latestLiveErrorState()
        tvLivesCount.text = requireContext().getString(R.string.not_available)

        tvShowAll.setTextColor(requireContext().getColor(R.color.app_grey_light))
        tvShowAll.isEnabled = false

        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    private fun latestLiveErrorState() = with(binding) {
        ivNewestLive.setImageResource(R.drawable.img_stream_error)
        btnPlay.visibility = View.GONE
        latestStreamProgressBar.visibility = View.GONE
        imgRvErrorImg.visibility = View.VISIBLE
        tvMessage.visibility = View.VISIBLE
    }

    private fun loadingState() = with(binding) {
        //recyclerview
        livesAdapter.submitList(emptyList())
        livesRecyclerProgressBar.visibility = View.VISIBLE
        imgRvErrorImg.visibility = View.GONE
        tvMessage.visibility = View.GONE
        //latest live
        btnPlay.visibility = View.GONE
        ivNewestLive.setImageDrawable(null)
        latestStreamProgressBar.visibility = View.VISIBLE

        tvShowAll.setTextColor(requireContext().getColor(R.color.app_grey_light))
        tvShowAll.isEnabled = false
    }

    private fun initGamesRecycler() = with(binding.rvGames) {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = gamesAdapter
    }

    private fun initLivesRecycler() = with(binding.rvLives) {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = livesAdapter
    }
}