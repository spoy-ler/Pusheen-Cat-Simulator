package com.example.pusheencatsimulator

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
//import android.support.constraint.ConstraintLayout
//import android.support.v4.content.res.ResourcesCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
//import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pusheencatsimulator.databinding.ActivityBathroomBinding


class BathroomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBathroomBinding
    private val maskDragMessage = ""
    private lateinit var catShower: MediaPlayer
    private lateinit var Timer: CountDownTimer
    private lateinit var TimerGif: CountDownTimer
    private lateinit var TimerBar: CountDownTimer
    var Tick: Boolean = false
    private lateinit var inAnimation: Animation
    private lateinit var fromAnimation: Animation


    private lateinit var adapter: AdapterBar

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBathroomBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_right)
        fromAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_left)
        catShower = MediaPlayer.create(this, R.raw.shower)

        updateLevel()
        (applicationContext as App).needs = (object : LEVELNEEDS {
            override fun needsinterface() {
                updateLevel()
            }
        })

        animgif()
        binding.backToMain.setOnClickListener {

            if (binding.backToCat.visibility == View.INVISIBLE) {
                binding.dragSoap.isEnabled = false
                binding.goToCat.clearAnimation()
                binding.catNonActiveGif.visibility = View.INVISIBLE
                binding.goToCat.visibility = View.INVISIBLE
                TimerGif.cancel()
                val animGif: ImageView = findViewById(R.id.back_to_cat)
                Glide.with(this)
                    .load(R.drawable.back_to_cat)
                    .into(animGif)
                binding.catNonActiveGif.visibility = View.INVISIBLE
                binding.catInBathroomNonActive.visibility = View.INVISIBLE
                binding.catInBathroomGif.visibility = View.INVISIBLE
                binding.backToCat.visibility = View.VISIBLE
                binding.backToCat.startAnimation(inAnimation)
                TimerGif = object : CountDownTimer(2000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        binding.backToCat.visibility = View.INVISIBLE
                        binding.dragSoap.isEnabled = true
                        onBackPressed()
                    }
                }
                TimerGif.start()
            }

        }
        attachViewDragListener()
        binding.dragSoap.setOnDragListener(maskDragListener)
//        binding.dragSoap.setOnDragListener { view, motionEvent ->
//            when (motionEvent.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    higherNeedsLevel()
//                    true
//                }
//                MotionEvent.ACTION_UP -> {
//                    TimerBar?.cancel()
//                    true
//                }
//                else -> false
//            }
//        }

    }

    fun updateLevel(){
        adapter = AdapterBar((applicationContext as App).levelValue)
        val layoutManager = GridLayoutManager(this, 12)
        binding.listView.layoutManager = layoutManager
        binding.listView.adapter = adapter

        if((applicationContext as App).levelValue >= 8) {
            binding.happyCatBar.visibility = View.VISIBLE
            binding.normalCatBar.visibility = View.INVISIBLE
            binding.sadCatBar.visibility = View.INVISIBLE
        }
        if((applicationContext as App).levelValue < 8 && (applicationContext as App).levelValue > 3) {
            binding.happyCatBar.visibility = View.INVISIBLE
            binding.normalCatBar.visibility = View.VISIBLE
            binding.sadCatBar.visibility = View.INVISIBLE
        }
        if((applicationContext as App).levelValue <= 3) {
            binding.happyCatBar.visibility = View.INVISIBLE
            binding.normalCatBar.visibility = View.INVISIBLE
            binding.sadCatBar.visibility = View.VISIBLE
        }
    }

    fun higherNeedsLevel(){
            var number = 0
            TimerBar = object : CountDownTimer(2000, 1500) {
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
                    TimerBar.start()
                }
            }.start()
            if (!(binding.dragSoap.isPressed)) TimerBar.cancel()
    }

    override fun onBackPressed(){

        (applicationContext as App).isCreatingActivity = true
        super.onBackPressed()
    }

    fun animgif() {
        val cat: ImageView = findViewById(R.id.cat_non_active_gif)
        Glide.with(this)
            .load(R.drawable.cat_non_active)
            .into(cat)
    }

    fun ImageOnClick()
    {
        if(Tick)
            Timer.cancel()
        catShower.start()
        binding.catInBathroomGif.setVisibility(View.VISIBLE)
        binding.bubblesLeft.alpha = 1F
        binding.bubblesRight.alpha = 1F
        binding.bubblesLeft.setVisibility(View.VISIBLE)
        binding.bubblesRight.setVisibility(View.VISIBLE)
        val imageView: ImageView = findViewById(R.id.cat_in_bathroom_gif)
        Glide.with(this)
            .load(R.drawable.cat_in_bathroom_active)
            .into(imageView)
        val bubblesLeft: ImageView = findViewById(R.id.bubbles_left)
        Glide.with(this)
            .load(R.drawable.bubbles)
            .into(bubblesLeft)
        val bubblesRight: ImageView = findViewById(R.id.bubbles_right)
        Glide.with(this)
            .load(R.drawable.bubbles)
            .into(bubblesRight)
        binding.catNonActiveGif.visibility = View.INVISIBLE

        binding.catInBathroomNonActive.setVisibility(View.INVISIBLE)
    }

    fun ResetImage()
    {
        if(catShower.isPlaying) {
            catShower.seekTo(0)
            catShower.pause()
        }

        var transparency = 1F
        Timer = object : CountDownTimer(3000, 10) {
            override fun onTick(millisUntilFinished: Long) {
                Tick = true
                transparency = transparency - 0.005F
                binding.bubblesLeft.alpha = transparency
                binding.bubblesRight.alpha = transparency
            }
            override fun onFinish() {
                Tick = false
                binding.bubblesLeft.setVisibility(View.INVISIBLE)
                binding.bubblesRight.setVisibility(View.INVISIBLE)
            }
        }
        Timer.start()
        binding.catInBathroomGif.setVisibility(View.INVISIBLE)
        binding.catInBathroomNonActive.setVisibility(View.VISIBLE)
        val imageView: ImageView = findViewById(R.id.cat_in_bathroom_non_active)
        Glide.with(this)
            .load(R.drawable.cat_in_bathroom_non_active)
            .into(imageView)

    }

    private val maskDragListener = View.OnDragListener { view, dragEvent ->

        val draggableItem = dragEvent.localState as View

        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                ImageOnClick()
                true
            }
//            DragEvent.ACTION_DRAG_ENTERED -> {
//                //binding.catNonActive.alpha = 0.3f
//                true
//            }
            DragEvent.ACTION_DRAG_EXITED -> {
                //binding.catNonActive.alpha = 1.0f
                draggableItem.visibility = View.VISIBLE
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                ResetImage()
                draggableItem.visibility = View.VISIBLE
                true
            }
            else -> {
                false
            }
        }
    }



    private fun attachViewDragListener() {

        //binding.catNonActiveGif.visibility = View.INVISIBLE
        //binding.catNonActive.visibility = View.VISIBLE

        binding.dragSoap.setOnLongClickListener { view: View ->
            higherNeedsLevel()
            val item = ClipData.Item(maskDragMessage)
            val dataToDrag = ClipData(
                maskDragMessage,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )
            val maskShadow = MaskDragShadowBuilder(view)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                @Suppress("DEPRECATION")
                view.startDrag(dataToDrag, maskShadow, view, 0)
            } else {
                view.startDragAndDrop(dataToDrag, maskShadow, view, 0)
            }
            view.visibility = View.VISIBLE
            true
        }
    }

    override fun  onPause() {

        super.onPause()

        if (!(applicationContext as App).isCreatingActivity)
            (applicationContext as App).stop1()
        TimerGif.cancel()
        binding.goToCat.clearAnimation()
        binding.goToCat.visibility = View.INVISIBLE
        binding.backToCat.clearAnimation()
        binding.backToCat.visibility = View.INVISIBLE

    }

    override fun onResume() {
        super.onResume()
        updateLevel()
        if (!(applicationContext as App).isCreatingActivity && !(applicationContext as App).musicStart)
            (applicationContext as App).start1()
        (applicationContext as App).isCreatingActivity = false
        binding.dragSoap.isEnabled = false
        val animGif: ImageView = findViewById(R.id.go_to_cat)
        Glide.with(this)
            .load(R.drawable.go_to_cat)
            .into(animGif)
        binding.catNonActiveGif.visibility = View.INVISIBLE
        binding.catInBathroomNonActive.visibility = View.INVISIBLE
        binding.catInBathroomGif.visibility = View.INVISIBLE
        binding.goToCat.visibility = View.VISIBLE
        binding.goToCat.startAnimation(fromAnimation)
        TimerGif = object : CountDownTimer(2300, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                binding.goToCat.visibility = View.INVISIBLE
                binding.catNonActiveGif.visibility = View.VISIBLE
                binding.dragSoap.isEnabled = true
            }
        }
        TimerGif.start()
    }

}

private class MaskDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {

    private val shadow = ResourcesCompat.getDrawable(view.context.resources, R.drawable.soap, view.context.theme)

    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        val width: Int = view.width
        val height: Int = view.height
        shadow?.setBounds(0, 0, width, height)
        size.set(width, height)
        touch.set(width / 2, height / 2)
    }

    override fun onDrawShadow(canvas: Canvas) {
        shadow?.draw(canvas)
    }

}