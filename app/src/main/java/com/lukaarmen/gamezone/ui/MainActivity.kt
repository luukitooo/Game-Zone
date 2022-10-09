package com.lukaarmen.gamezone.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkSession()
    }

    // TODO: AuthenticationManager.isLoggedIn() instead of static boolean
    private fun checkSession() {
        navigation(true)
    }

    private fun navigation(isLoggedIn: Boolean) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        when (isLoggedIn) {
            true -> navController.navigate(R.id.tabsFragment)
            false -> navController.navigate(R.id.welcomeFragment)
        }
    }


//    override fun onBackPressed() {
//        if (backPressedTime + 3000 > System.currentTimeMillis()) {
//            super.onBackPressed()
//            finish()
//        } else {
//            Toast.makeText(this, R.string.double_backpress, Toast.LENGTH_LONG).show()
//        }
//        backPressedTime = System.currentTimeMillis()
//    }


}