package com.lukaarmen.gamezone.ui.tabs.home.livematcheslist

import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.databinding.FragmentLiveMatchesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LiveMatchesListFragment : BaseFragment<FragmentLiveMatchesListBinding>(
    FragmentLiveMatchesListBinding::inflate
) {

    private val viewModel by viewModels<LiveMatchesListViewModel>()
    private val livesAdapter: LivesAdapter by lazy { LivesAdapter() }

    private var isSearching = false
    private var searchJob: Job? = null

    override fun init() {
        return
    }

    override fun listeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSearch.setOnClickListener {
            isSearching = !isSearching
            setSearching(isSearching)
        }
        binding.etSearch.doOnTextChanged { matchName, _, _, _ ->
            doInBackground {
                searchFor(matchName.toString())
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

    private fun searchFor(matchName: String) {
        searchJob?.cancel()
        searchJob = doInBackground(Dispatchers.Main) {
            delay(500L)
            viewModel.fetchMatches(
                name = matchName
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
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.livesState.collect {
                it.data?.let { matchesList ->
                    initLivesRecycler()
                    livesAdapter.submitList(matchesList)
                    binding.progressBar.isVisible = false
                }
                it.error?.let { error ->
                    Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
                it.isLoading?.let { isLoading ->
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