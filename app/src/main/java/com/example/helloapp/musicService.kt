package com.example.helloapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.provider.Settings
import android.util.Log

class MusicService : Service() {
    private lateinit var mp: MediaPlayer
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        mp = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        mp.isLooping = true
        Log.d("MusicService", "MediaPlayer initialized")
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!mp.isPlaying) {
            mp.start()
            Log.d("MusicService", "MediaPlayer started")
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        if (mp.isPlaying) {
            mp.stop()
            Log.d("MusicService", "MediaPlayer stopped")
        }
        mp.release()
//        mp.stop()
        super.onDestroy()
    }
}
