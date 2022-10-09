package com.lukaarmen.gamezone.ui.tabs.leagues.leaguesfragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lukaarmen.gamezone.R

class LeaguesFragment : Fragment() {

    companion object {
        fun newInstance() = LeaguesFragment()
    }

    private lateinit var viewModel: LeaguesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leagues, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LeaguesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}