package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import android.util.Log.d
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.getStreamPreview
import com.lukaarmen.gamezone.common.utils.CategoryIndicator
import com.lukaarmen.gamezone.common.utils.GameType
import com.lukaarmen.gamezone.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    private val viewModel by viewModels<HomeViewModel>()
    private val gamesAdapter: GamesAdapter by lazy { GamesAdapter() }
    private val livesAdapter: LivesHomeAdapter by lazy { LivesHomeAdapter() }

    private val gamesList = listOf(
        CategoryIndicator(GameType.ALL, false),
        CategoryIndicator(GameType.CSGO, false),
        CategoryIndicator(GameType.DOTA2, false),
        CategoryIndicator(GameType.OWERWATCH, false),
        CategoryIndicator(GameType.RAINBOW_SIX, false),
    )

    override fun init() {
        initGamesRecycler()
        initLivesRecycler()
        gamesAdapter.submitList(gamesList)
        doInBackground {
            viewModel.getAllRunningMatches()
        }
    }

    override fun listeners() {
        binding.tvShowAll.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLiveMatchesListFragment())
        }

        gamesAdapter.onClickListener = { gameType ->
            doInBackground {
                if (gameType.title == GameType.ALL.title) {
                    viewModel.getAllRunningMatches()
                } else {
                    viewModel.getLivesByGame(gameType.title)
                }
            }
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.viewState.collect {
                if (it.isLoading!!) {
                    d("homeFragment_loading", it.isLoading.toString())
                    livesAdapter.submitList(emptyList())
                    binding.livesRecyclerProgressBar.visibility = View.VISIBLE
                }
                if (it.data != null) {
                    if (it.data.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(it.data[0].streamsList?.last()?.embedUrl?.getStreamPreview())
                            .into(binding.ivNewestLive)
                    }

                    livesAdapter.submitList(it.data)
                    binding.tvLivesCount.text = it.data.size.toString()
                    binding.livesRecyclerProgressBar.visibility = View.GONE
                    d("homeFragment_success", it.data.toString())
                }
                if (it.error != "") {
                    binding.livesRecyclerProgressBar.visibility = View.GONE
                    d("homeFragment_error", it.error.toString())
                }
            }
        }
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