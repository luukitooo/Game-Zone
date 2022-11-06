package com.lukaarmen.gamezone.ui.auth.login

import android.content.Intent
import android.util.Log.e
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.areLinesEmpty
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.makeLink
import com.lukaarmen.gamezone.databinding.FragmentLoginBinding
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {

    companion object {
        const val GOOGLE_SIGN_IN = 10
    }

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var auth: FirebaseAuth

    override fun init() {
        return
    }

    override fun listeners() = with(binding) {
        btnForgotPassword.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            )
        }
        btnLogin.setOnClickListener {
            handleLoginRequest()
        }
        btnSignInWithGoogle.setOnClickListener {
            signInWithGoogle()
        }
        tvCreateAccount.makeLink(
            Pair("Create one", View.OnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
                )
            })
        )
    }

    override fun observers() {
        doInBackground {
            viewModel.loginSuccessFlow.collect { isSuccessful ->
                handleLoginResponse(isSuccessful)
            }
        }
        doInBackground {
            viewModel.googleSignInSuccessFlow.collect { isSuccessful ->
                handleGoogleSignInResponse(isSuccessful)
            }
        }
    }

    private fun handleLoginRequest() = with(binding) {
        if (!areLinesEmpty(etEmail, etPassword)){
            doInBackground {
                viewModel.loginWith(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            }
        } else {
            Snackbar.make(root, "Lines are empty...", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun handleLoginResponse(isSuccessful: Boolean) {
        if (isSuccessful) {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToTabsFragment()
            )
        } else {
            Snackbar.make(binding.root, "Invalid user...", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun handleGoogleSignInResponse(isSuccessful: Boolean) {
        if (isSuccessful) {
            doInBackground {
                viewModel.saveNewUser(
                    User(
                        uid = auth.currentUser!!.uid,
                        email = auth.currentUser!!.email
                    )
                )
            }.invokeOnCompletion {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToEnterUsernameFragment(null)
                )
            }
        } else {
            Snackbar.make(binding.root, "Can't sign in with google...", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun signInWithGoogle() {
        val googleSignInIntent = googleSignInClient.signInIntent
        startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            viewModel.handleGoogleSignInTask(data)
        }
    }

}