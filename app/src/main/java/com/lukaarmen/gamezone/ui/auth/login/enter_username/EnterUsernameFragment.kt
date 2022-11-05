package com.lukaarmen.gamezone.ui.auth.login.enter_username

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.databinding.FragmentEnterUsernameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterUsernameFragment :
    BaseFragment<FragmentEnterUsernameBinding>(FragmentEnterUsernameBinding::inflate) {

    private val viewModel: EnterUsernameViewModel by viewModels()

    private val args by navArgs<EnterUsernameFragmentArgs>()

    override fun init() {
        args.username?.let { binding.etUsername.setText(it) }
    }

    override fun listeners() = with(binding) {
        btnConfirm.setOnClickListener {
            changeUsername(
                newUsername = binding.etUsername.text.toString()
            )
        }
    }

    override fun observers() {
        return
    }

    private fun changeUsername(newUsername: String) {
        if (newUsername.isNotEmpty() && newUsername != args.username) {
            doInBackground {
                viewModel.updateUsername(
                    newUsername = newUsername
                )
                Snackbar.make(binding.root, "Username updated", Snackbar.LENGTH_SHORT).show()
            }.invokeOnCompletion {
                args.username?.let {findNavController().popBackStack()}
            }
        }
        else
            Snackbar.make(binding.root, "Please enter new username", Snackbar.LENGTH_SHORT).show()
    }

}