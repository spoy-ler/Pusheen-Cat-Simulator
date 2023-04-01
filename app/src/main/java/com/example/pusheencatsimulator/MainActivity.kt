package com.example.pusheencatsimulator

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pusheencatsimulator.databinding.ActivityMainBinding
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var catSleep: MediaPlayer
    private lateinit var catPlay: MediaPlayer
    private lateinit var TimerGif: CountDownTimer
    private lateinit var Timer: CountDownTimer
    private lateinit var TimerBar: CountDownTimer

    private lateinit var inAnimation: Animation
    private lateinit var fromAnimation: Animation
    private lateinit var adapter: AdapterBar
    var volumeValueSleep = 1.0F
    var volumeValuePlay = 0.4F
    var toBathroom: Boolean = false
    var toKitchen: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_right)
        fromAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_left)

        Log.d("path123", this.getExternalFilesDir(null)!!.absolutePath)

//        adapter = AdapterBar(0)
//        val layoutManager = GridLayoutManager(this, 12)
//        binding.listView.layoutManager = layoutManager
//        binding.listView.adapter = adapter
//        object : CountDownTimer(12000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                adapter = AdapterBar((millisUntilFinished/1000).toInt())
//                val layoutManager = GridLayoutManager(this@MainActivity, 12)
//                binding.listView.layoutManager = layoutManager
//                binding.listView.adapter = adapter
//
//            }
//            override fun onFinish() {
//
//            }
//        }.start()

//        adapter = AdapterBar(8)
//        val layoutManager = GridLayoutManager(this, 12)
//        binding.listView.layoutManager = layoutManager
//        binding.listView.adapter = adapter


        Timer = object : CountDownTimer(3500, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                binding.onClickPlayFirst.setVisibility(View.INVISIBLE)
                binding.catSleepActive.setVisibility(View.INVISIBLE)
                binding.nonActiveImg.setVisibility(View.VISIBLE)
                higherNeedsLevel()
            }
        }
        catSleep = MediaPlayer.create(this, R.raw.cat_sleep_2)
        catSleep.setVolume(volumeValueSleep,volumeValueSleep)
        catPlay = MediaPlayer.create(this, R.raw.urring_cat)
        catPlay.setVolume(volumeValuePlay,volumeValuePlay)
        binding.nonActiveImg.setImageResource(R.drawable.cat_non_active)
        animgif()
        
        binding.playButton.setOnClickListener(View.OnClickListener {

            higherNeedsLevel()
            //Timer.cancel()
            binding.goToCat.clearAnimation()
            binding.goToCat.visibility = View.INVISIBLE
            binding.nonActiveImg.visibility = View.INVISIBLE
            if (binding.backToCat.visibility == View.INVISIBLE) {
                TimerGif.cancel()
                val imageView: ImageView = findViewById(R.id.on_click_play_first)
                Glide.with(this)
                    .load(R.drawable.cat_to_pet)
                    .into(imageView)
                stopSound()
                catPlay.start()
                binding.onClickPlayFirst.setVisibility(View.VISIBLE)
                //binding.nonActiveImg.setVisibility(View.INVISIBLE)
                binding.catSleepActive.setVisibility(View.INVISIBLE)
                Timer.start()
            }

        })

        binding.sleepButton.setOnClickListener(View.OnClickListener {

            higherNeedsLevel()
            //Timer.cancel()
            binding.goToCat.clearAnimation()
            binding.goToCat.visibility = View.INVISIBLE
            binding.nonActiveImg.visibility = View.INVISIBLE
            if (binding.backToCat.visibility == View.INVISIBLE) {
                TimerGif.cancel()
                val imageView: ImageView = findViewById(R.id.cat_sleep_active)
                Glide.with(this)
                    .load(R.drawable.cat_sleep_active)
                    .into(imageView)
                stopSound()
                catSleep.start()
                binding.catSleepActive.setVisibility(View.VISIBLE)
                binding.onClickPlayFirst.setVisibility(View.INVISIBLE)
                //binding.nonActiveImg.setVisibility(View.INVISIBLE)
                Timer.start()
            }
        })

        binding.toBathroomButton.setOnClickListener {
            stopSound()
            Timer.cancel()
            binding.nonActiveImg.visibility = View.INVISIBLE
            binding.catSleepActive.visibility = View.INVISIBLE
            binding.onClickPlayFirst.visibility = View.INVISIBLE
            toBathroom = true
            if (binding.backToCat.visibility == View.INVISIBLE)
                backToCat()
        }

        binding.toKitchenButton.setOnClickListener{
            stopSound()
            Timer.cancel()
            binding.nonActiveImg.visibility = View.INVISIBLE
            binding.catSleepActive.visibility = View.INVISIBLE
            binding.onClickPlayFirst.visibility = View.INVISIBLE
            toKitchen = true
            if (binding.backToCat.visibility == View.INVISIBLE)
                backToCat()
        }

        /////////////////////////

        updateLevel()
        (applicationContext as App).needs = (object : LEVELNEEDS {
            override fun needsinterface() {
                updateLevel()
            }
        })

//        binding.normalCatBar.setOnClickListener{
//            (applicationContext as App).levelValue = 9
//        }
        /////////////////////////
    }

    /////////////////////////////

    fun updateLevel(){
        adapter = AdapterBar((applicationContext as App).levelValue)
        val layoutManager = GridLayoutManager(this, 12)
        binding.listView.layoutManager = layoutManager
        binding.listView.adapter = adapter
    }

    fun higherNeedsLevel(){
        var number = 0
        TimerBar = object : CountDownTimer(1500, 500) {
            override fun onTick(millisUntilFinished: Long) {
                if ((applicationContext as App).levelValue < 12) {
                    if ((applicationContext as App).levelValue > 10 && number != 3)
                    {
                        (applicationContext as App).levelValue = 12
                        number++
                    }
                    else if(number != 3) {
                        (applicationContext as App).levelValue = (applicationContext as App).levelValue + 1
                        number++

                    }
                }
                updateLevel()
            }
            override fun onFinish() {
//                if (binding.nonActiveImg.visibility == View.VISIBLE)
//                TimerBar.start()
            }
        }.start()
//        if (binding.nonActiveImg.visibility == View.VISIBLE)
//            TimerBar.cancel()
    }

    ////////////////////////////

    override fun  onPause() {

        super.onPause()
        if (!(applicationContext as App).isCreatingActivity)
            (applicationContext as App).stop1()
        TimerGif.cancel()
        binding.goToCat.clearAnimation()
        binding.goToCat.visibility = View.INVISIBLE
        binding.backToCat.clearAnimation()
        binding.backToCat.visibility = View.INVISIBLE
        //(applicationContext as App).writeFile()
    }



    override fun onResume() {
        super.onResume()
        updateLevel()
        if (!(applicationContext as App).isCreatingActivity && !(applicationContext as App).musicStart)
            (applicationContext as App).start1()
        (applicationContext as App).isCreatingActivity = false
        val animGif: ImageView = findViewById(R.id.go_to_cat)
        Glide.with(this)
            .load(R.drawable.go_to_cat)
            .into(animGif)
        binding.nonActiveImg.visibility = View.INVISIBLE
        binding.catSleepActive.visibility = View.INVISIBLE
        binding.onClickPlayFirst.visibility = View.INVISIBLE
        binding.goToCat.visibility = View.VISIBLE
        binding.goToCat.startAnimation(fromAnimation)
        TimerGif = object : CountDownTimer(2300, 2300) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                binding.goToCat.visibility = View.INVISIBLE
                if (binding.backToCat.visibility == View.INVISIBLE)
                    binding.nonActiveImg.visibility = View.VISIBLE
            }
        }
        TimerGif.start()
    }

    fun backToCat() {
        binding.goToCat.clearAnimation()
        binding.goToCat.visibility = View.INVISIBLE
        binding.nonActiveImg.visibility = View.INVISIBLE

        val animGif: ImageView = findViewById(R.id.back_to_cat)
        Glide.with(this)
            .load(R.drawable.back_to_cat)
            .into(animGif)
        binding.catSleepActive.visibility = View.INVISIBLE
        binding.onClickPlayFirst.visibility = View.INVISIBLE
        binding.backToCat.visibility = View.VISIBLE
        binding.backToCat.startAnimation(inAnimation)
        TimerGif = object : CountDownTimer(2000, 2000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                binding.backToCat.visibility = View.INVISIBLE
                TimerGif.cancel()
                (applicationContext as App).isCreatingActivity = true
                if (toBathroom) {
                    toBathroom = false
                    val intent = Intent(this@MainActivity, BathroomActivity::class.java)
                    startActivity(intent)
                }
                if (toKitchen) {
                    toKitchen = false
                    val intent = Intent(this@MainActivity, KitchenActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        TimerGif.start()
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

    override fun onDestroy() {
        super.onDestroy()
        //(applicationContext as App).stopSevice()
    }



    override fun onBackPressed() = Unit
}