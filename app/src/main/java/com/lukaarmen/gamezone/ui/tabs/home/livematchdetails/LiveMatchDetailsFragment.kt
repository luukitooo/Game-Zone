package com.lukaarmen.gamezone.ui.tabs.home.livematchdetails

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
import com.lukaarmen.gamezone.common.extentions.*
import com.lukaarmen.gamezone.common.utils.GameType
import com.lukaarmen.gamezone.databinding.FragmentLiveMatchDetailsBinding
import com.lukaarmen.gamezone.models.Match
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LiveMatchDetailsFragment : BaseFragment<FragmentLiveMatchDetailsBinding>(
    FragmentLiveMatchDetailsBinding::inflate
) {

    private val args: LiveMatchDetailsFragmentArgs by navArgs()

    private val viewModel by viewModels<LiveMatchDetailsViewModel>()
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
                Snackbar.make(binding.root, "Live is not available", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.btnShare.setOnClickListener {
            findNavController().navigate(
                LiveMatchDetailsFragmentDirections.actionLiveMatchDetailsFragmentToShareBottomSheet(
                    matchId = args.matchId
                )
            )
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

    private fun errorState(error: String) = with(binding) {
        progressBar.isVisible = false
        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAction("Reload") {
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
            "CS:GO" -> GameType.CSGO.gameWallpaper
            "Dota 2" -> GameType.DOTA2.gameWallpaper
            "Overwatch" -> GameType.OWERWATCH.gameWallpaper
            "Rainbow 6 Siege" -> GameType.RAINBOW_SIX.gameWallpaper
            else -> R.drawable.ic_error
        }
    }

}