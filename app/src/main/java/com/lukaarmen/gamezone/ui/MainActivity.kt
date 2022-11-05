package com.lukaarmen.gamezone.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GameZone)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}