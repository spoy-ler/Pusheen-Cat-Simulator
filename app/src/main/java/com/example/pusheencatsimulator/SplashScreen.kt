package com.example.pusheencatsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.pusheencatsimulator.databinding.ActivityMainBinding
import com.example.pusheencatsimulator.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var Timer: CountDownTimer
    private lateinit var binding: ActivitySplashScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Thread{
            Thread.sleep(4100L)
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)
        }.start()
        var count = 0


        val imageView: ImageView = findViewById(R.id.loading_img)
        Glide.with(this)
            .load(R.drawable.loading_img)
            .into(imageView)


        Timer = object : CountDownTimer(550, 10) {
            override fun onTick(millisUntilFinished: Long) {
                if (count == 2)
                binding.firstDot.visibility = View.VISIBLE
                if (count == 3)
                binding.secondDot.visibility = View.VISIBLE
                if (count == 4)
                binding.thirdDot.visibility = View.VISIBLE
            }
            override fun onFinish() {
                if (binding.firstDot.visibility == View.VISIBLE && binding.secondDot.visibility == View.VISIBLE && binding.thirdDot.visibility == View.VISIBLE) {
                    binding.firstDot.visibility = View.INVISIBLE
                    binding.secondDot.visibility = View.INVISIBLE
                    binding.thirdDot.visibility = View.INVISIBLE
                    count = 0
                }
                count++

                Timer.start()
            }
        }
        Timer.start()
    }
}