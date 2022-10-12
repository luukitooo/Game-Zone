package com.lukaarmen.gamezone.ui.tabs.leagues.seriesfragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lukaarmen.gamezone.R

class SeriesFragment : Fragment() {

    companion object {
        fun newInstance() = SeriesFragment()
    }

    private lateinit var viewModel: SeriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_series, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SeriesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}