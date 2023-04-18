package ru.pgk63.core_common.extension

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "ComposableNaming")
@Composable
fun <T>Flow<T>.launchWhenStarted(){
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            this@launchWhenStarted.collect()
        }
    }
}