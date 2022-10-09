package com.lukaarmen.gamezone.ui.tabs.home.livematchdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lukaarmen.gamezone.R

class LiveMatchDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = LiveMatchDetailsFragment()
    }

    private lateinit var viewModel: LiveMatchDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live_match_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LiveMatchDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}