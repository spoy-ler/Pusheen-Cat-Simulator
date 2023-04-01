package com.example.pusheencatsimulator

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.IBinder

class ServiceBackgroundMusic : Service() {

    private lateinit var backgroundMusic: MediaPlayer
    var volumeValue = 0.2F

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music)
        backgroundMusic.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        backgroundMusic.start()
        backgroundMusic.setVolume(volumeValue,volumeValue)


//        object : CountDownTimer(10000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//
//            }
//            override fun onFinish() {
//                test()
//            }
//        }.start()

        return super.onStartCommand(intent, flags, startId)
    }



    override fun onDestroy() {
        super.onDestroy()
        backgroundMusic.stop()
        backgroundMusic.release()
    }


//    fun test() {
//        (applicationContext as App).test.testinterface()
//    }
}