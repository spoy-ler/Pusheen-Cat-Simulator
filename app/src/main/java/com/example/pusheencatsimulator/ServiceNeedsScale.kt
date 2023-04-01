package com.example.pusheencatsimulator

import android.app.AlarmManager
import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.util.Timer
import kotlin.math.floor
import kotlin.math.round
import kotlin.properties.Delegates


class ServiceNeedsScale : Service() {

    var file : File by Delegates.notNull()
    var fileTime : File by Delegates.notNull()

    private lateinit var TimerService: CountDownTimer

    var currentTime = System.currentTimeMillis() // поточний час
    var targetTime = currentTime + 15000 // час, до якого потрібно відлічити (наприклад, 60 секунд з поточного часу)
    var previousTime : Int = currentTime.toInt()


    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        try {
            file = File(this.getExternalFilesDir(null)!!.absolutePath, "nameFile.txt")

            if (file.createNewFile()) {
                val levelValue = (applicationContext as App).levelValue.toString()
                file.writeText(levelValue)
            } else {
                //file.writeText("12")
                (applicationContext as App).levelValue = file.bufferedReader().readLine().toString().toInt()
                updateLevelNeeds()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


//        TimerService = object : CountDownTimer(10000, 10) {
//            override fun onTick(millisUntilFinished: Long) { }
//            override fun onFinish() {
//                lowerLevelNeeds()
//                try {
//                    val levelValue = (applicationContext as App).levelValue.toString()
//                    file.writeText(levelValue)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }.start()


        //////////////////////////////////////////


        TimerService = object : CountDownTimer(targetTime - currentTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // виконується кожну секунду, поки таймер не закінчиться
            }

            override fun onFinish() {
                updateDestroyLevel()
                lowerLevelNeeds()

                try {
                    val levelValue = (applicationContext as App).levelValue.toString()
                    file.writeText(levelValue)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                currentTime = System.currentTimeMillis() // поточний час
                targetTime = currentTime + 10000
                try {
                    val timeValue = currentTime.toString()
                    fileTime.writeText(timeValue)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start() // запускаємо таймер

        /////////////////////////////////////////

        return super.onStartCommand(intent, flags, startId)
    }

    fun updateDestroyLevel(){
        fileTime = File(this.getExternalFilesDir(null)!!.absolutePath, "fileTime.txt")



        try {
            if (fileTime.createNewFile()) {
                val timeValue = currentTime.toString()
                fileTime.writeText(timeValue)
            } else {
                //file.writeText("12")
                previousTime = fileTime.bufferedReader().readLine().toString().toInt()
                if (currentTime.toInt() != previousTime)
                    (applicationContext as App).levelValue = (currentTime.toInt() - previousTime)/10000
                updateLevelNeeds()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        try {
            val timeValue = currentTime.toString()
            fileTime.writeText(timeValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //(applicationContext as App).writeFile()
//        try {
//            (applicationContext as App).levelValue = 6
//            val levelValue = (applicationContext as App).levelValue.toString()
//            file.writeText(levelValue)
//            } catch (e: Exception) {
//            e.printStackTrace()
//        }

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    fun lowerLevelNeeds() {

        if ((applicationContext as App).levelValue > 0) {
            (applicationContext as App).levelValue = (applicationContext as App).levelValue - 1
        }

        TimerService.start()
        updateLevelNeeds()
    }

    fun updateLevelNeeds() {
        (applicationContext as App).needs.needsinterface()
    }
}