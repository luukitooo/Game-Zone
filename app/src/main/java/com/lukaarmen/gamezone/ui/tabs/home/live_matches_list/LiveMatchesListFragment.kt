package com.lukaarmen.gamezone.ui.tabs.home.live_matches_list

import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extension.doInBackground
import com.lukaarmen.gamezone.common.extension.hide
import com.lukaarmen.gamezone.databinding.FragmentLiveMatchesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LiveMatchesListFragment : BaseFragment<FragmentLiveMatchesListBinding>(
    FragmentLiveMatchesListBinding::inflate
) {

    private val viewModel by viewModels<LiveMatchesListViewModel>()
    private val livesAdapter: LivesAdapter by lazy { LivesAdapter() }

    private var isSearching = false

    override fun init() {
        initLivesRecycler()
    }

    override fun listeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSearch.setOnClickListener {
            isSearching = !isSearching
            setSearching(isSearching)
        }
        binding.etSearch.doOnTextChanged { matchName, start, before, _ ->
            if(start != 0 || before != 0){
                doInBackground {
                    search(matchName.toString())
                }
            }
        }
        livesAdapter.onClickListener = {
            findNavController().navigate(
                LiveMatchesListFragmentDirections.actionLiveMatchesListFragmentToLiveMatchDetailsFragment(
                    it
                )
            )
        }
    }

    private fun setSearching(isSearching: Boolean): Unit = with(binding) {
        if (isSearching) {
            btnSearch.setImageResource(R.drawable.ic_cross)
            etSearch.isVisible = true
        } else {
            btnSearch.setImageResource(R.drawable.ic_search)
            etSearch.isVisible = false
            etSearch.setText("")
        }
    }

    private fun search(matchName: String?){
        doInBackground {
            delay(500L)
            livesAdapter.submitList(emptyList())
            viewModel.fetchMatches(matchName)
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.livesState.collect { viewState ->
                viewState.data?.let { matchesList ->
                    livesAdapter.submitList(matchesList).also {
                        binding.livesRecycler.startLayoutAnimation()
                    }
                    binding.progressBar.hide()
                }
                viewState.error?.let { error ->
                    Snackbar.make(binding.root, getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG)
                        .setAnchorView(binding.guideline)
                        .setAction(getString(R.string.reload)) {
                        doInBackground { viewModel.fetchMatches() }
                    }.show()
                    binding.progressBar.hide()
                }
                viewState.isLoading?.let { isLoading ->
                    binding.progressBar.isVisible = isLoading
                }
            }
        }
    }

    private fun initLivesRecycler() = with(binding.livesRecycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = livesAdapter
    }
}