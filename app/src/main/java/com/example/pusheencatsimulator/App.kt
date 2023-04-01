package com.example.pusheencatsimulator

import android.app.Application
import android.content.Intent
import android.util.Log
import java.io.File
import kotlin.properties.Delegates

class App: Application() {

    var isCreatingActivity = true
    var musicStart = false
    private lateinit var intent1: Intent
    private lateinit var intent2: Intent
    var levelValue : Int = 12
    var file : File by Delegates.notNull()

    lateinit var needs: LEVELNEEDS

    fun start1(){
        startService(intent1)
        musicStart = true
    }

    fun stop1(){
        stopService(intent1)
        musicStart = false
    }

//    fun writeFile(){
//        //(applicationContext as App).levelValue = 5
//        var readValue : String = (applicationContext as App).levelValue.toString()
//        try {
//            //(applicationContext as App).levelValue = 1
//            //val levelValue = (applicationContext as App).levelValue.toString()
//            file.writeText(readValue)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        //levelValue = readValue.toInt()
//
//    }


    override fun onCreate() {
        super.onCreate()

        levelValue = readFile()

        val intent = Intent(this, ServiceBackgroundMusic::class.java)
        intent1 = intent
        start1()
        intent2 = Intent(this, ServiceNeedsScale::class.java)
        startService(intent2)

    }

    fun readFile() : Int{
        var readValue : String = "12"
        try {
            file = File(this.getExternalFilesDir(null)!!.absolutePath, "nameFile.txt")
            readValue = file.bufferedReader().readLine().toString()
        } catch (e: Exception) {
            Log.d("Exception1", e.toString())
        }
        return readValue.toInt()
    }

//    fun writeFileValue() : Int{
//        var readValue : String = "12"
//        try {
//            //(applicationContext as App).levelValue = 1
//            val levelValue = (applicationContext as App).levelValue.toString()
//            file.writeText(levelValue)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return readValue.toInt()
//    }

}

interface LEVELNEEDS{
    fun needsinterface()
}