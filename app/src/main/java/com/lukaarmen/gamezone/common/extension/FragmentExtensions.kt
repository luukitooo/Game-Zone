package com.lukaarmen.gamezone.common.extension

import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.lukaarmen.gamezone.R

//navigate from tabs fragments to main graph
fun Fragment.findTopNavController(): NavController {
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}

// check EditTexts
fun Fragment.areLinesEmpty(vararg editTexts: EditText): Boolean {
    editTexts.forEach {
        if (it.text!!.isEmpty())
            return true
    }
    return false
}