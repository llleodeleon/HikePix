package com.leodeleon.hikepix

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leodeleon.hikepix.service.HikeService
import com.leodeleon.hikepix.ui.HikeScreen
import com.leodeleon.hikepix.ui.theme.HikePixTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HikePixTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HikeScreen(
                        viewModel = viewModel(),
                        onStartHike = {
                            sendAction(HikeService.ACTION_START)
                        },
                        onStopHike = {
                            sendAction(HikeService.ACTION_STOP)
                        })
                }
            }
        }
    }

    private fun sendAction(action: String) {
        Intent(this, HikeService::class.java).also {
            it.action = action
            startService(it)
        }
    }
}
