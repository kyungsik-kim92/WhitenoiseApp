package com.example.whitenoiseapp.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import com.example.whitenoiseapp.constants.Constants
import com.example.whitenoiseapp.model.PlayModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WhiteNoiseService : Service() {
    private var timer: CountDownTimer? = null

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private val _timerState = MutableSharedFlow<TimerState>()
    val timerState = _timerState.asSharedFlow()

    private var whiteNoiseList: List<PlayModel> = ArrayList()
    private var mediaPlayerList: ArrayList<MediaPlayer?> = ArrayList()

    inner class WhiteNoiseBinder : Binder() {
        fun getService() = this@WhiteNoiseService
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

    fun setupTimer(ms: Long) {
        timer?.cancel()
        if (!isPlaying()) {
            return
        }

        if (ms <= 0L) {
            return
        }
        timer = object : CountDownTimer(ms, 1000) {
            override fun onTick(ms: Long) {
                serviceScope.launch {
                    _timerState.emit(TimerState.Update(ms))
                }
            }

            override fun onFinish() {
                serviceScope.launch {
                    mediaPlayerList.forEachIndexed { index, _ -> stopMediaPlayer(index) }
                }
            }
        }.start()
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


    sealed class TimerState {
        data class Update(val ms: Long) : TimerState()
        data object Finish : TimerState()
    }
}

