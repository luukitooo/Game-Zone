package com.lukaarmen.gamezone.ui.profile.profilefragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.fragment.app.viewModels
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

    private val requestReadStorageLauncher = registerForActivityResult(
        RequestPermission(),
        ::onGotReadStoragePermissionResult
    )

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
            requestReadStorageLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun observers() {
        updateProfile()
    }

    private fun updateProfile() {
        doInBackground {
            viewModel.userSate.collect { user ->
                binding.tvEmail.text = user?.email
                binding.tvUserId.text = user?.uid
                binding.tvUsername.text = user?.displayName
                //binding.ivUser.setImageURI(user?.photoUrl)
            }
        }
    }

    private fun onGotReadStoragePermissionResult(granted: Boolean) {
        if (granted) {
            result.launch("image/*")
            Toast.makeText(requireContext(), "granted", Toast.LENGTH_SHORT).show()
        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                askUserForOpeningAppSettings()
            } else {
                Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun askUserForOpeningAppSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireActivity().packageName, this.toString())
        )
        if (requireActivity().packageManager.resolveActivity(
                appSettingsIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            Toast.makeText(requireContext(), "permission denied forever", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("permission denied")
                .setMessage("permission denied forever")
                .setPositiveButton("Open") { _, _ ->
                    startActivity(appSettingsIntent)
                }
                .create()
                .show()
        }
    }

    private val result =
        registerForActivityResult(GetContent()) { result ->
            result?.let {
                binding.ivUser.setImageURI(it)
            }
        }

}