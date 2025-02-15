package com.example.whitenoiseapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager

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

    override fun onCreate() {
        super.onCreate()
        whiteNoiseList = Constants.getPlayList()
        setupMediaPlayList(whiteNoiseList)

    }

    private fun setupMediaPlayList(whiteNoiseList: List<PlayModel>) {
        whiteNoiseList.forEachIndexed { index, playModel ->
            val musicTitle = resources.getResourceName(playModel.musicResId)
            val resId = resources.getIdentifier(musicTitle, "raw", packageName)
            mediaPlayerList.add(
                index,
                MediaPlayer.create(this, resId).apply {
                    isLooping = true
                }
            )
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayerList.any { it?.isPlaying == true }
    }

    fun startMediaPlayer(index: Int) {
        if (mediaPlayerList[index]?.isPlaying != true) {
            mediaPlayerList[index]?.start()
        }
    }

    fun stopMediaPlayer(index: Int) {
        if (mediaPlayerList[index]?.isPlaying == true) {
            mediaPlayerList[index]?.pause()
            mediaPlayerList[index]?.seekTo(0)
        }
    }

    private fun sendBroadcast(ms: Long, action: String) {
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent().apply {
            putExtra("ms", ms)
            this.action = action
        })
    }
}