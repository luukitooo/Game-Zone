package com.lukaarmen.gamezone.ui.auth.login

import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.databinding.FragmentEnterUsernameBinding
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class EnterUsernameFragment : BaseFragment<FragmentEnterUsernameBinding>(FragmentEnterUsernameBinding::inflate) {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    @Named("Users")
    lateinit var usersReference: DatabaseReference

    override fun init() {
        return
    }

    override fun listeners() = with(binding) {
        btnConfirm.setOnClickListener {
            if (etUsername.text!!.isNotEmpty()) {
                saveUserToDatabase()
                findNavController().navigate(
                    EnterUsernameFragmentDirections.actionEnterUsernameFragmentToTabsFragment()
                )
            } else {
                Snackbar.make(root, "Please enter your username...", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun observers() {
        return
    }

    private fun saveUserToDatabase() {
        usersReference.child(firebaseAuth.currentUser?.uid!!)
            .setValue(
                User(
                    firebaseAuth.currentUser?.uid!!,
                    firebaseAuth.currentUser?.email!!,
                    binding.etUsername.text.toString()
                )
            )
    }

}