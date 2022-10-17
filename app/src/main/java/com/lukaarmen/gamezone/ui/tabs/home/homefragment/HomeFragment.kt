package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import android.util.Log.d
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.getStreamPreview
import com.lukaarmen.gamezone.common.utils.CategoryIndicator
import com.lukaarmen.gamezone.common.utils.GameType
import com.lukaarmen.gamezone.databinding.FragmentHomeBinding
import com.lukaarmen.gamezone.models.Match
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    private val viewModel by viewModels<HomeViewModel>()
    private val gamesAdapter: GamesAdapter by lazy { GamesAdapter() }
    private val livesAdapter: LivesHomeAdapter by lazy { LivesHomeAdapter() }

    private val gamesList = mutableListOf(
        CategoryIndicator(GameType.ALL, false),
        CategoryIndicator(GameType.CSGO, false),
        CategoryIndicator(GameType.DOTA2, false),
        CategoryIndicator(GameType.OWERWATCH, false),
        CategoryIndicator(GameType.RAINBOW_SIX, false),
    )

    override fun init() {
        initGamesRecycler()
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
                when (gameType.title) {
                    GameType.ALL.title -> viewModel.getAllRunningMatches()
                    else -> viewModel.getLivesByGame(gameType.title)
                }
            }
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.viewState.collect {
                if (it.isLoading!!) {
                    loadingState()
                    d("homeFragment_loading", it.isLoading.toString())
                }
                if (it.data != null) {
                    successfulState(it.data)
                    d("homeFragment_success", it.data.toString())
                }
                if (it.error != "") {
                    errorState()
                    d("homeFragment_error", it.error.toString())
                }
            }
        }

        doInBackground {
            viewModel.streamsCountState.collect {
                binding.tvLivesCount.text = it.toString()
            }
        }

    }

    private fun successfulState(data: List<Match>) = with(binding) {
        val list = data.take(6).toMutableList()
        initLivesRecycler()
        when (data.size) {
            0 -> {
                ivNewestLive.setImageResource(R.drawable.ic_error)
                imgRvErrorImg.visibility = View.VISIBLE
                tvMessage.isVisible = true
                tvMessage.text = "No Data"
            }
            1 -> {
                Glide.with(requireContext())
                    .load(data[0].streamsList?.last()?.embedUrl?.getStreamPreview())
                    .into(ivNewestLive)
                btnPlay.visibility = View.VISIBLE
            }
            else -> {
                Glide.with(requireContext())
                    .load(data[0].streamsList?.last()?.embedUrl?.getStreamPreview())
                    .into(ivNewestLive)
                list.removeAt(0)
                btnPlay.visibility = View.VISIBLE
            }
        }


        livesAdapter.submitList(list)
        livesRecyclerProgressBar.visibility = View.GONE
        latestStreamProgressBar.visibility = View.GONE
    }

    private fun errorState() = with(binding) {
        //recyclerview
        livesAdapter.submitList(emptyList())
        livesRecyclerProgressBar.visibility = View.GONE
        imgRvErrorImg.visibility = View.VISIBLE
        tvMessage.visibility = View.VISIBLE
        tvMessage.text = "Something went wrong"
        //latest live
        ivNewestLive.setImageResource(R.drawable.ic_error)
        btnPlay.visibility = View.GONE
        tvLivesCount.text = "N\\A"
        latestStreamProgressBar.visibility = View.GONE
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

}