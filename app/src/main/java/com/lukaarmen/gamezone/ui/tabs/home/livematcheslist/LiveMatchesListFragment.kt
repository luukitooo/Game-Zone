package com.lukaarmen.gamezone.ui.tabs.home.livematcheslist

import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
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
import kotlinx.coroutines.Job

@AndroidEntryPoint
class LiveMatchesListFragment : BaseFragment<FragmentLiveMatchesListBinding>(
    FragmentLiveMatchesListBinding::inflate
) {

    private val viewModel by viewModels<LiveMatchesListViewModel>()
    private val livesAdapter: LivesAdapter by lazy { LivesAdapter() }

    private var isSearching = false

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
        binding.etSearch.doAfterTextChanged { matchName->
            doInBackground {
                viewModel.setSearchInput(matchName.toString())
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

    override fun observers() {
        doInBackground {
            viewModel.livesState.collect { viewState ->
                viewState.data?.let { matchesList ->
                    initLivesRecycler()
                    livesAdapter.submitList(matchesList).also {
                        binding.livesRecycler.startLayoutAnimation()
                    }
                    binding.progressBar.isVisible = false
                }
                viewState.error?.let { error ->
                    Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAction("Reload") {
                        doInBackground { viewModel.fetchMatches() }
                    }.show()
                    binding.progressBar.isVisible = false
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