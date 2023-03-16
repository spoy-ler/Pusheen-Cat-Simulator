package com.example.pusheencatsimulator

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.example.pusheencatsimulator.databinding.ActivityMainBinding
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var catSleep: MediaPlayer
    private lateinit var catPlay: MediaPlayer
    private lateinit var Timer: CountDownTimer
    var volumeValueSleep = 1.0F
    var volumeValuePlay = 0.4F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timer = object : CountDownTimer(3500, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                binding.onClickPlayFirst.setVisibility(View.INVISIBLE)
                binding.catSleepActive.setVisibility(View.INVISIBLE)
                binding.nonActiveImg.setVisibility(View.VISIBLE)
            }
        }
        catSleep = MediaPlayer.create(this, R.raw.cat_sleep_2)
        catSleep.setVolume(volumeValueSleep,volumeValueSleep)
        catPlay = MediaPlayer.create(this, R.raw.urring_cat)
        catPlay.setVolume(volumeValuePlay,volumeValuePlay)
        binding.nonActiveImg.setImageResource(R.drawable.cat_non_active)
        animgif()
        
        binding.playButton.setOnClickListener(View.OnClickListener {
            val imageView: ImageView = findViewById(R.id.on_click_play_first)
            Glide.with(this).load(R.drawable.cat_to_pet).into(imageView)
            stopSound()
            catPlay.start()
            binding.onClickPlayFirst.setVisibility(View.VISIBLE)
            binding.nonActiveImg.setVisibility(View.INVISIBLE)
            binding.catSleepActive.setVisibility(View.INVISIBLE)
            Timer.start()
        })

        binding.sleepButton.setOnClickListener(View.OnClickListener {
            val imageView: ImageView = findViewById(R.id.cat_sleep_active)
            Glide.with(this).load(R.drawable.cat_sleep_active).into(imageView)
            stopSound()
            catSleep.start()
            binding.catSleepActive.setVisibility(View.VISIBLE)
            binding.onClickPlayFirst.setVisibility(View.INVISIBLE)
            binding.nonActiveImg.setVisibility(View.INVISIBLE)
            Timer.start()
        })

        binding.toBathroomButton.setOnClickListener {

            stopSound()
            (applicationContext as App).isCreatingActivity = true
            val intent = Intent(this@MainActivity, BathroomActivity::class.java)
            startActivity(intent)
        }

        binding.toKitchenButton.setOnClickListener{
            stopSound()
            (applicationContext as App).isCreatingActivity = true
            val intent = Intent(this@MainActivity, KitchenActivity::class.java)
            startActivity(intent)
        }

    }

    override fun  onPause() {

        super.onPause()

        if (!(applicationContext as App).isCreatingActivity)
            (applicationContext as App).stop1()

    }

    override fun onResume() {
        super.onResume()
        if (!(applicationContext as App).isCreatingActivity && !(applicationContext as App).musicStart)
            (applicationContext as App).start1()
        (applicationContext as App).isCreatingActivity = false
    }

    fun animgif() {
        val imageView: ImageView = findViewById(R.id.non_active_img)
        Glide.with(this)
            .load(R.drawable.cat_non_active)
            .into(imageView)
    }

    fun stopSound() {
        if(catSleep.isPlaying) {
            catSleep.seekTo(0)
            catSleep.pause()
        }
        if(catPlay.isPlaying) {
            catPlay.seekTo(0)
            catPlay.pause()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.onClickPlayFirst.setVisibility(View.INVISIBLE)
        binding.nonActiveImg.setVisibility(View.VISIBLE)
        binding.catSleepActive.setVisibility(View.INVISIBLE)
    }

    override fun onBackPressed() = Unit
}