package com.lukaarmen.gamezone.ui.profile.profilefragment

import android.graphics.drawable.BitmapDrawable
import android.util.Log.d
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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

        settingsAdapter.onClickListener = {
            d("myLog", it.toString())
        }
//        btnSignOut.setOnClickListener {
//            Snackbar.make(binding.root, "Do you want to sign Out?", Snackbar.LENGTH_LONG)
//                .setAction("Yes") {
//                    viewModel.signOut()
//                    findTopNavController().navigate(R.id.welcomeFragment)
//                }.show()
//        }
//        btnChangeProfilePhoto.setOnClickListener {
//            imagePickerResult.launch("image/*")
//        }
    }

    override fun observers(): Unit = with(binding) {
        doInBackground {
            viewModel.userSate.collect { user ->
                d("user_log", user.imageUrl ?: "Photo")
                user.apply {
                    email?.let {
                        tvEmail.text = it
                    }
                    username?.let {
                        tvUsername.text = it
                    }
                    ivUser.setProfilePhoto(user.imageUrl, imageProgressbar)
                }
            }
        }

        doInBackground {
            viewModel.photoUploadProgressFlow.collect{
                if(it > 0) binding.imageProgressbar.show()
                binding.imageProgressbar.progress = it.toInt()
            }
        }
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

}