package com.lukaarmen.gamezone.ui.tabs.home.live_matches_list.match_details

import android.content.Intent
import android.net.Uri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extension.*
import com.lukaarmen.gamezone.common.util.GameTitle
import com.lukaarmen.gamezone.common.util.GameType
import com.lukaarmen.gamezone.databinding.FragmentLiveMatchDetailsBinding
import com.lukaarmen.gamezone.model.Match
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MatchDetailsFragment : BaseFragment<FragmentLiveMatchDetailsBinding>(
    FragmentLiveMatchDetailsBinding::inflate
) {

    private val args: MatchDetailsFragmentArgs by navArgs()

    private val viewModel by viewModels<MatchDetailsViewModel>()
    private val playersAdapter: PlayersAdapter by lazy { PlayersAdapter() }
    private var liveLink = ""

    override fun init() {
        initRecyclerView()
    }

    override fun listeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnWatch.setOnClickListener {
            try{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(liveLink)
                startActivity(intent)
            }catch (e: Throwable){
                Snackbar.make(binding.root, getString(R.string.live_is_not_available), Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.btnShare.setOnClickListener {
            findNavController().navigate(
                MatchDetailsFragmentDirections.actionLiveMatchDetailsFragmentToShareBottomSheet(
                    matchId = args.matchId
                )
            )
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.matchState.collect {
                it.data?.let { match -> successState(match) }
                it.error?.let { error -> errorState() }
                it.isLoading?.let { loadingState() }
            }
        }

        doInBackground {
            viewModel.matchPlayers.collect {
                playersAdapter.submitList(it)
                delay(1)
                binding.rvPlayers.startLayoutAnimation()

            }
        }
    }

    private fun successState(match: Match) = with(binding) {

        if(match.streamsList != null && match.streamsList.isNotEmpty()){
            liveLink = match.streamsList.last()?.rawUrl.toString()
        }
        progressBar.isVisible = false

        if(match.status == "running"){
            tvLiveNow.show()
            btnWatch.isEnabled = true
        }

            ivLive.setImageDrawable(requireContext().getDrawable(setImage(match.videoGame?.name)))

        ivTeamFirst.setPhotoByUrl(match.opponents?.first()?.imageUrl, firstTeamProgressBar, R.drawable.ic_no_image)
        ivTeamSecond.setPhotoByUrl(match.opponents?.last()?.imageUrl, secondTeamProgressBar, R.drawable.ic_no_image)

        tvScoreFirst.text = match.results?.first()?.score.toString()
        tvScoreSecond.text = match.results?.last()?.score.toString()

        tvTitle.text = match.name
        tvStartTime.text = match.beginAt.toString().filterDate()

    }

    private fun errorState() = with(binding) {
        progressBar.isVisible = false
        Snackbar.make(root, getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG)
            .setAnchorView(guideline)
            .setAction("Reload") {
            doInBackground { viewModel.getMatchById() }
        }.show()
    }

    private fun loadingState() = with(binding) {
        progressBar.isVisible = true
    }

    private fun initRecyclerView() = with(binding.rvPlayers) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = playersAdapter
    }

    private fun setImage(videoGame: String?): Int {
        return when (videoGame) {
            GameTitle.CSGO.title -> GameType.CSGO.gameWallpaper
            GameTitle.DOTA_2.title -> GameType.DOTA2.gameWallpaper
            GameTitle.OVERWATCH.title -> GameType.OWERWATCH.gameWallpaper
            GameTitle.RAINBOW_6_SIEGE.title -> GameType.RAINBOW_SIX.gameWallpaper
            else -> R.drawable.ic_error
        }
    }

}