package com.example.whitenoiseapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder

class WhiteNoiseService : Service() {

    private var whiteNoiseList: List<PlayModel> = ArrayList()
    private var mediaPlayerList: ArrayList<MediaPlayer?> = ArrayList()
    private var timer: CountDownTimer? = null


    inner class WhiteNoiseBinder : Binder() {
        fun getService(): WhiteNoiseService {
            return this@WhiteNoiseService
        }
    }

    override fun onBind(p0: Intent?): IBinder = WhiteNoiseBinder()


    private fun setupMediaPlayerList(playList: List<PlayModel>) {

    }
}