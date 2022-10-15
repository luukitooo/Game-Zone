package com.lukaarmen.gamezone.ui

import android.os.Bundle
import android.util.Log.d
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lukaarmen.gamezone.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.test().collect{
                it.onFailure {error ->
                    d("mylog_main_error", error)
                }
                it.onLoader {loading ->
                    d("mylog_main_loading", loading.toString())
                }
                it.onSuccess {result ->
                    result.forEach{model ->
                        d("mylog_main_success", model.name.toString())
                    }
                }
            }
        }
    }

}