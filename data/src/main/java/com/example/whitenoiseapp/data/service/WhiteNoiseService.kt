package com.example.whitenoiseapp.data.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import com.example.whitenoiseapp.domain.model.PlayModel
import com.example.whitenoiseapp.domain.model.TimerState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class WhiteNoiseService @Inject constructor(
) : Service() {
    @Inject
    @Named("PlayList")
    lateinit var whiteNoiseList: List<PlayModel>

    private var timer: CountDownTimer? = null
    private var remainingTimeMs: Long = 0L
    private var isTimerPaused: Boolean = false

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private val _timerState = MutableSharedFlow<TimerState>()
    val timerState = _timerState.asSharedFlow()

    private var mediaPlayerList: ArrayList<MediaPlayer?> = ArrayList()

    fun getRemainingTime(): Long = remainingTimeMs

    inner class WhiteNoiseBinder : Binder() {
        fun getService() = this@WhiteNoiseService
    }

    override fun onBind(p0: Intent?): IBinder {
        return WhiteNoiseBinder()
    }

    override fun onCreate() {
        super.onCreate()
        if (::whiteNoiseList.isInitialized) {
            setupMediaPlayList(whiteNoiseList)
        }
    }

    private fun setupMediaPlayList(playList: List<PlayModel>) {
        playList.forEachIndexed { index, playModel ->
            try {
                val musicTitle = resources.getResourceName(playModel.musicResId)
                val resId = resources.getIdentifier(musicTitle, "raw", packageName)

                if (resId != 0) {
                    val mediaPlayer = MediaPlayer.create(this, resId)?.apply {
                        isLooping = true
                    }
                    mediaPlayerList.add(index, mediaPlayer)
                } else {
                    mediaPlayerList.add(index, null)
                }
            } catch (e: Exception) {
                mediaPlayerList.add(index, null)
            }
        }
    }

    fun setupTimer(ms: Long) {
        timer?.cancel()
        remainingTimeMs = ms
        isTimerPaused = false

        if (!isPlaying()) {
            return
        }

        if (ms <= 0L) {
            return
        }

        startTimer(ms)
    }

    private fun startTimer(ms: Long) {
        timer = object : CountDownTimer(ms, 1000) {
            override fun onTick(ms: Long) {
                remainingTimeMs = ms
                serviceScope.launch {
                    _timerState.emit(TimerState.Update(ms))
                }
            }

            override fun onFinish() {
                remainingTimeMs = 0L
                isTimerPaused = false
                serviceScope.launch {
                    mediaPlayerList.forEachIndexed { index, _ -> stopMediaPlayer(index) }
                    _timerState.emit(TimerState.Finish)
                }
            }
        }.start()
    }

    fun isPlaying(): Boolean {
        val playing = mediaPlayerList.any { it?.isPlaying == true }
        return playing
    }

    private fun pauseTimer() {
        if (!isTimerPaused && remainingTimeMs > 0) {
            timer?.cancel()
            isTimerPaused = true
            serviceScope.launch {
                _timerState.emit(TimerState.Paused(remainingTimeMs))
            }
        }
    }

    private fun resumeTimer() {
        if (isTimerPaused && remainingTimeMs > 0) {
            isTimerPaused = false
            startTimer(remainingTimeMs)
            serviceScope.launch {
                _timerState.emit(TimerState.Resumed(remainingTimeMs))
            }
        }
    }

    fun startMediaPlayer(index: Int) {

        val wasPlaying = isPlaying()
        val mediaPlayer = mediaPlayerList.getOrNull(index)

        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying) {
                try {
                    mediaPlayer.start()
                } catch (e: Exception) {
                }
            }
        } else {
        }

        if (!wasPlaying && isPlaying() && isTimerPaused) {
            resumeTimer()
        }
    }

    fun stopMediaPlayer(index: Int) {
        val mediaPlayer = mediaPlayerList.getOrNull(index)
        if (mediaPlayer != null && mediaPlayer.isPlaying) {
            try {
                mediaPlayer.pause()
                mediaPlayer.seekTo(0)
            } catch (e: Exception) {
            }
        }

        if (!isPlaying() && !isTimerPaused && remainingTimeMs > 0) {
            pauseTimer()
        }
    }
}
