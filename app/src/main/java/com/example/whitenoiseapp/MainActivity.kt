package com.example.whitenoiseapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.whitenoiseapp.databinding.ActivityMainBinding
import com.example.whitenoiseapp.service.WhiteNoiseService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var whiteNoiseService: WhiteNoiseService

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as WhiteNoiseService.WhiteNoiseBinder
            whiteNoiseService = binder.getService()

        }

        override fun onServiceDisconnected(arg0: ComponentName) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
                ?.findNavController()
        navController?.let {
            binding.navView.setupWithNavController(it)
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(this, WhiteNoiseService::class.java),
            mConnection,
            Context.BIND_AUTO_CREATE
        )
    }
}