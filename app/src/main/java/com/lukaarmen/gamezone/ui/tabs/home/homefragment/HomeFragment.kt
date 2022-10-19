package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.getStreamPreview
import com.lukaarmen.gamezone.common.utils.CategoryIndicator
import com.lukaarmen.gamezone.common.utils.GameType
import com.lukaarmen.gamezone.common.utils.gamesList
import com.lukaarmen.gamezone.databinding.FragmentHomeBinding
import com.lukaarmen.gamezone.models.Match
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    private val viewModel by viewModels<HomeViewModel>()
    private val gamesAdapter: GamesAdapter by lazy { GamesAdapter() }
    private val livesAdapter: LivesHomeAdapter by lazy { LivesHomeAdapter() }

    override fun init() {
        initGamesRecycler()
        gamesAdapter.submitList(gamesList)
    }

    override fun listeners() {
        binding.tvShowAll.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLiveMatchesListFragment())
        }

        gamesAdapter.onClickListener = { gameType ->
            updateGamesList(gameType)
            when (gameType.title) {
                GameType.ALL.title -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.getAllRunningMatches()
                    }
                }
                else -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.getLivesByGame(gameType.title)
                    }
                }
            }
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.viewState.collectLatest {
                it.data?.let { matches -> successfulState(matches) }
                it.error?.let { error -> errorState() }
                it.isLoading?.let { loadingState() }
            }
        }
    }

    private fun successfulState(data: List<Match>) = with(binding) {
        val list = data.take(6).toMutableList()
        initLivesRecycler()
        when (data.size) {
            0 -> {
                latestLiveErrorState()
                tvMessage.text = requireContext().getString(R.string.no_data)
            }
            else -> {
                if (data.size != 1) list.removeAt(0)
                Glide.with(requireContext())
                    .load(data[0].streamsList?.last()?.embedUrl?.getStreamPreview())
                    .into(ivNewestLive)
                btnPlay.visibility = View.VISIBLE
            }
        }

        binding.tvLivesCount.text = data.size.toString()
        livesAdapter.submitList(list)
        livesRecyclerProgressBar.visibility = View.GONE
        latestStreamProgressBar.visibility = View.GONE
    }

    private fun errorState() = with(binding) {
        //recyclerview
        livesAdapter.submitList(emptyList())
        livesRecyclerProgressBar.visibility = View.GONE
        tvMessage.text = requireContext().getString(R.string.error)
        //latest live
        latestLiveErrorState()
        tvLivesCount.text = requireContext().getString(R.string.not_available)
    }

    private fun latestLiveErrorState() = with(binding) {
        ivNewestLive.setImageResource(R.drawable.ic_error)
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
    }

    private fun initGamesRecycler() = with(binding.rvGames) {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = gamesAdapter
    }

    private fun initLivesRecycler() = with(binding.rvLives) {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = livesAdapter
    }

    private fun updateGamesList(gameType: GameType) {
        var position = 0
        val newList = mutableListOf<CategoryIndicator>()
        newList.addAll(gamesList)

        newList.forEach {
            if (it.gameType == gameType) position = newList.indexOf(it)
            if (it.isSelected) {
                newList[newList.indexOf(it)] =
                    CategoryIndicator(gameType = it.gameType, isSelected = false)
            }
        }
        newList[position] =
            CategoryIndicator(gameType = gamesList[position].gameType, isSelected = true)
        gamesAdapter.submitList(newList)
    }
}