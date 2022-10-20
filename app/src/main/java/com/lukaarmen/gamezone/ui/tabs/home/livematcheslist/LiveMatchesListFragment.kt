package com.lukaarmen.gamezone.ui.tabs.home.livematcheslist

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.databinding.FragmentLiveMatchesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LiveMatchesListFragment : BaseFragment<FragmentLiveMatchesListBinding>(
    FragmentLiveMatchesListBinding::inflate
) {

    private val viewModel by viewModels<LiveMatchesListViewModel>()
    private val livesAdapter: LivesAdapter by lazy { LivesAdapter() }

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
            viewModel.viewState.collect {
                it.data?.let { matchesList ->
                    initLivesRecycler()
                    livesAdapter.submitList(matchesList)
                    binding.progressBar.isVisible = false
                }
                it.error?.let { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
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