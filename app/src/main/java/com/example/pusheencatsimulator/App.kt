package com.example.pusheencatsimulator

import android.app.Application
import android.content.Intent

class App: Application() {

    var isCreatingActivity = true
    var musicStart = false
    private lateinit var intent1: Intent

    fun start1(){
        startService(intent1)
        musicStart = true
    }

    fun stop1(){
        stopService(intent1)
        musicStart = false
    }

    override fun onCreate() {
        super.onCreate()

        val intent = Intent(this, ServiceBackgroundMusic::class.java)
        intent1 = intent
        start1()

    }

}