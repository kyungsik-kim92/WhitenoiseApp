package com.example.whitenoiseapp.ui.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.whitenoiseapp.data.datasource.service.MediaPlayerDataSource
import com.example.whitenoiseapp.data.service.WhiteNoiseService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var whiteNoiseService: WhiteNoiseService
    private val mainViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var mediaPlayerDataSource: MediaPlayerDataSource

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as WhiteNoiseService.WhiteNoiseBinder
            whiteNoiseService = binder.getService()
            mainViewModel.setServiceReady(true)
            mediaPlayerDataSource.setService(whiteNoiseService)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mainViewModel.setServiceReady(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(this, WhiteNoiseService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        try {
            unbindService(serviceConnection)
        } catch (e: Exception) {
        }
    }
}




