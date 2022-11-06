package com.lukaarmen.gamezone.ui.profile.profilefragment

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.util.Log.d
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.findTopNavController
import com.lukaarmen.gamezone.common.extentions.setPhotoByUrl
import com.lukaarmen.gamezone.common.extentions.show
import com.lukaarmen.gamezone.databinding.FragmentProfileBinding
import com.lukaarmen.gamezone.ui.profile.profilefragment.adapters.SettingsAdapter
import com.lukaarmen.gamezone.ui.profile.profilefragment.settings.SettingsType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(
    FragmentProfileBinding::inflate
) {

    private val viewModel by viewModels<ProfileViewModel>()
    private val settingsAdapter: SettingsAdapter by lazy { SettingsAdapter() }
    private var userName: String? = null

    override fun init() {
        binding.settingsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = settingsAdapter
            settingsAdapter.submitList(viewModel.settings)
        }
        doInBackground {
            viewModel.updateProfile()
        }
    }

    override fun listeners() = with(binding) {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        settingsAdapter.notificationListener = {isChecked ->
            d("myLog", isChecked.toString())
        }

        settingsAdapter.onClickListener = { settingType ->
            when (settingType) {
                SettingsType.USERNAME -> changeUsername()
                SettingsType.PASSWORD -> changePassword()
                SettingsType.PHOTO -> imagePickerResult.launch("image/*")
                SettingsType.SIGN_OUT -> signOut()
                SettingsType.HELP -> contactUs()
                SettingsType.ABOUT_US -> aboutUs()
                else -> d("myLog", settingType.toString())
            }
        }
    }

    override fun observers(): Unit = with(binding) {
        doInBackground {
            viewModel.userSate.collect { user ->
                user.apply {
                    email?.let {
                        tvEmail.text = it
                    }
                    username?.let {
                        userName = it
                        tvUsername.text = it
                    }
                    ivUser.setPhotoByUrl(user.imageUrl, imageProgressbar, R.drawable.ic_user)
                }
            }
        }
    }

    private fun changeUsername() {
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToEnterUsernameFragment(
                userName
            )
        )
    }

    private fun changePassword() {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToNewPasswordFragment())
    }

    private val imagePickerResult =
        registerForActivityResult(GetContent()) { Uri ->
            Uri?.let {
                binding.imageProgressbar.show()
                val drawable = BitmapDrawable.createFromStream(
                    requireActivity().contentResolver.openInputStream(it), it.path
                )
                viewModel.uploadImageToStorage(drawable as BitmapDrawable)
            }
        }

    private fun signOut() {
        Snackbar.make(binding.root, "Do you want to sign Out?", Snackbar.LENGTH_LONG)
            .setAction("Yes") {
             viewModel.signOut()
             findTopNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToWelcomeFragment())
            }.show()
    }

    private fun contactUs(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("gamezonedevelop@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact us")
        intent.type = "message/rfc822"
        startActivity(Intent.createChooser(intent, "Choose an Email client :"))
    }

    private fun aboutUs(){
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAboutUsFragment())
    }
}