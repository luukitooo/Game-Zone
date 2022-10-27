package com.lukaarmen.gamezone.ui.auth.login

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.databinding.FragmentEnterUsernameBinding
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class EnterUsernameFragment :
    BaseFragment<FragmentEnterUsernameBinding>(FragmentEnterUsernameBinding::inflate) {

    private val args by navArgs<EnterUsernameFragmentArgs>()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    @Named("Users")
    lateinit var usersReference: DatabaseReference

    override fun init() {
        args.username?.let { binding.etUsername.setText(it) }
    }

    override fun listeners() = with(binding) {
        btnConfirm.setOnClickListener {
            if (etUsername.text!!.isNotEmpty()) {
                if(args.username == null){
                    saveUserToDatabase()
                    findNavController().navigate(
                        EnterUsernameFragmentDirections.actionEnterUsernameFragmentToTabsFragment()
                    )
                }else{
                    Snackbar.make(root, "Username successfully changed", Snackbar.LENGTH_LONG).show()
                    changeUserName()
                    findNavController().popBackStack()
                }
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
                    uid = firebaseAuth.currentUser?.uid!!,
                    email = firebaseAuth.currentUser?.email!!,
                    username = binding.etUsername.text.toString()
                )
            )
    }

    private fun changeUserName() {
        usersReference
            .child(firebaseAuth.currentUser!!.uid)
            .child("username")
            .setValue(binding.etUsername.text.toString())
    }

}