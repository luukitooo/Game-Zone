package com.lukaarmen.gamezone.ui.profile.profilefragment

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.findTopNavController
import com.lukaarmen.gamezone.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(
    FragmentProfileBinding::inflate
) {

    private val viewModel by viewModels<ProfileViewModel>()

    override fun init() {
        return
    }

    override fun listeners() {
        binding.btnSignOut.setOnClickListener {
            Snackbar.make(binding.root, "Do you want to sign Out?", Snackbar.LENGTH_LONG)
                .setAction("Yes") {
                    viewModel.signOut()
                    findTopNavController().navigate(R.id.welcomeFragment)
                }.show()
        }

        binding.ivUser.setOnClickListener {
            imagePickerResult.launch("image/*")
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
                        tvUsername.text = it
                    }
                    imageUrl?.let {
                        Glide.with(root).load(it).into(ivUser)
                    }
                    uid?.let {
                        tvUserId.text = "ID: $it"
                    }
                }
            }
        }
    }

    private val imagePickerResult =
        registerForActivityResult(GetContent()) { result ->
            result?.let {
                val drawable = BitmapDrawable.createFromStream(requireActivity().contentResolver.openInputStream(it), it.path)
                doInBackground { viewModel.uploadImageToStorage(drawable as BitmapDrawable) }
            }
        }

}