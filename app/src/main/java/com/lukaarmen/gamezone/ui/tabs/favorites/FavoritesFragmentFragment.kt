package com.lukaarmen.gamezone.ui.tabs.favorites

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extension.doInBackground
import com.lukaarmen.gamezone.common.extension.hide
import com.lukaarmen.gamezone.common.extension.show
import com.lukaarmen.gamezone.databinding.FragmentFavoritesBinding
import com.lukaarmen.gamezone.model.FavoriteLeague
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragmentFragment : BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    private val viewModel: FavoritesViewModel by viewModels()

    @Inject
    lateinit var auth: FirebaseAuth

    private val favoriteLeagueAdapter = FavoriteLeagueAdapter()

    private var animationStarted = false
    private var lastRemovedLeague: FavoriteLeague? = null
    private var undoSnackBar: Snackbar? = null

    override fun init() {
        binding.rvFavoriteLeagues.adapter = favoriteLeagueAdapter
        setItemTouchHelperTo(binding.rvFavoriteLeagues)
    }

    override fun listeners() {
        favoriteLeagueAdapter.onItemClickListener = { league ->
            findNavController().navigate(
                FavoritesFragmentFragmentDirections.actionFavoritesFragmentFragmentToMatchesFragment2(
                    leagueId = league.leagueId,
                    gameType = league.gameType
                )
            )
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.favoriteLeaguesFlow(auth.currentUser!!.uid).collect { domainLeagues ->
                if (domainLeagues.isEmpty()) { binding.layoutError.show() }
                else { binding.layoutError.hide() }
                favoriteLeagueAdapter.submitList(
                    domainLeagues.map { domain ->
                        FavoriteLeague.fromFavoriteLeagueDomain(domain)
                    }
                ).also {
                    if (!animationStarted) {
                        binding.rvFavoriteLeagues.startLayoutAnimation()
                        animationStarted = true
                    }
                }
            }
        }
    }

    private fun setItemTouchHelperTo(recyclerView: RecyclerView) {
        val simpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                handleItemSwipe(viewHolder)
            }
        }
        ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView)
    }

    private fun handleItemSwipe(viewHolder: RecyclerView.ViewHolder) {
        val league = favoriteLeagueAdapter.currentList[viewHolder.adapterPosition]
        lastRemovedLeague = league
        doInBackground { viewModel.removeLeague(league) }
        createUndoSnackBar().show()
    }

    private fun createUndoSnackBar(): Snackbar {
        return Snackbar.make(
            binding.root,
            getString(R.string.return_confirmation),
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.undo)) {
            doInBackground {
                viewModel.addLeague(lastRemovedLeague!!)
            }
        }.setAnchorView(binding.glLayoutBottom).also {
            undoSnackBar = it
        }
    }

    override fun onPause() {
        super.onPause()
        undoSnackBar?.dismiss()
    }

}