package com.lukaarmen.gamezone.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.domain.repositories.firebase.UsersRepository
import com.lukaarmen.domain.usecases.users.UpdateUserActivityUseCase
import com.lukaarmen.gamezone.common.utils.ActivityStatus
import com.lukaarmen.gamezone.databinding.ActivityMainBinding
import com.lukaarmen.gamezone.common.workers.SetStatusToOfflineWorker
import com.lukaarmen.gamezone.common.workers.SetStatusToOnlineWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}