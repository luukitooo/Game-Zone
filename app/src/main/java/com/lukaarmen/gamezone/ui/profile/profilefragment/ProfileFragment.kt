package com.lukaarmen.gamezone.ui.profile.profilefragment

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
import com.lukaarmen.gamezone.common.extentions.setProfilePhoto
import com.lukaarmen.gamezone.common.extentions.show
import com.lukaarmen.gamezone.databinding.FragmentProfileBinding
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
            settingsAdapter.submitList(settings)
        }

        return
    }

    override fun listeners() = with(binding) {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        settingsAdapter.onClickListener2 = { settingType ->
            when (settingType) {
                SettingType.USERNAME -> changeUsername()
                SettingType.PASSWORD -> changePassword()
                SettingType.PHOTO -> imagePickerResult.launch("image/*")
                SettingType.SIGN_OUT -> signOut()
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
                    ivUser.setProfilePhoto(user.imageUrl, imageProgressbar)
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
                findTopNavController().navigate(R.id.welcomeFragment)
            }.show()
    }
}