package com.lukaarmen.gamezone.ui.tabs.home.livematcheslist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lukaarmen.gamezone.R

class LiveMatchesListFragment : Fragment() {

    companion object {
        fun newInstance() = LiveMatchesListFragment()
    }

    private lateinit var viewModel: LiveMatchesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live_matches_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LiveMatchesListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}