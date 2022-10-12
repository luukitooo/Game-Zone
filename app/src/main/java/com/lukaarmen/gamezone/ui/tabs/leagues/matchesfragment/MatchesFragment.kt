package com.lukaarmen.gamezone.ui.tabs.leagues.matchesfragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lukaarmen.gamezone.R

class MatchesFragment : Fragment() {

    companion object {
        fun newInstance() = MatchesFragment()
    }

    private lateinit var viewModel: MatchesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_matches, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MatchesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}