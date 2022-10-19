package com.lukaarmen.gamezone.common.extentions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun LifecycleOwner.doInBackground(
    context: CoroutineContext = EmptyCoroutineContext,
    action: suspend (CoroutineScope) -> Unit
): Job {
    return when (this) {
        is Fragment -> {
            viewLifecycleOwner.lifecycleScope.launch(context) {
                action(this@launch)
            }
        }
        else -> {
            lifecycleScope.launch {
                action(this@launch)
            }
        }
    }
}