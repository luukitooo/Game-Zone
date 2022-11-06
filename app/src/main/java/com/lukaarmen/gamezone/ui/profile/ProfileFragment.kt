package com.lukaarmen.gamezone.ui.profile

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log.d
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extension.doInBackground
import com.lukaarmen.gamezone.common.extension.findTopNavController
import com.lukaarmen.gamezone.common.extension.setPhotoByUrl
import com.lukaarmen.gamezone.common.extension.show
import com.lukaarmen.gamezone.databinding.FragmentProfileBinding
import com.lukaarmen.gamezone.ui.profile.adapter.SettingAdapter
import com.lukaarmen.gamezone.ui.profile.settings.SettingsType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(
    FragmentProfileBinding::inflate
) {

    private val viewModel by viewModels<ProfileViewModel>()
    private val settingAdapter: SettingAdapter by lazy { SettingAdapter() }
    private var userName: String? = null

    override fun init() {
        binding.settingsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = settingAdapter
            settingAdapter.submitList(viewModel.settings)
        }
        doInBackground {
            viewModel.updateProfile()
        }
    }

    override fun listeners() = with(binding) {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        settingAdapter.onClickListener = { settingType ->
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
                    ivUser.setPhotoByUrl(user.imageUrl, null, R.drawable.img_guest)
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
                val drawable = BitmapDrawable.createFromStream(
                    requireActivity().contentResolver.openInputStream(it), it.path
                )
                viewModel.uploadImageToStorage(drawable as BitmapDrawable)
            }
        }

    private fun signOut() {
        Snackbar.make(binding.root, getString(R.string.confirm_sign_out), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.yes)) {
             viewModel.signOut()
             findTopNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToWelcomeFragment())
            }.show()
    }

    private fun contactUs(){
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("gamezonedevelop@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_us))
        intent.data = Uri.parse("mailto:")
        startActivity(intent)
    }

    private fun aboutUs(){
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAboutUsFragment())
    }
}