package com.example.pusheencatsimulator

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.media.MediaPlayer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.constraint.ConstraintLayout
import android.support.v4.content.res.ResourcesCompat
import android.view.DragEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.pusheencatsimulator.databinding.ActivityKitchenBinding

class KitchenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKitchenBinding
    private val maskDragMessage = ""
    private lateinit var catFeed: MediaPlayer
    var volumeValue = 0.7F
    private lateinit var inAnimation: Animation
    private lateinit var fromAnimation: Animation
    private lateinit var TimerGif: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityKitchenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_right)
        fromAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_left)
        catFeed = MediaPlayer.create(this, R.raw.cat_feed)
        catFeed.setVolume(volumeValue,volumeValue)
        animgif()
        val imageView: ImageView = findViewById(R.id.drag_bowl)
        Glide.with(this)
            .load(R.drawable.bowl)
            .into(imageView)
        binding.backToMain.setOnClickListener {
            if (binding.backToCat.visibility == View.INVISIBLE) {
                binding.dragBowl.isEnabled = false
                binding.goToCat.clearAnimation()
                binding.goToCat.visibility = View.INVISIBLE
                TimerGif.cancel()
                val animGif: ImageView = findViewById(R.id.back_to_cat)
                Glide.with(this)
                    .load(R.drawable.back_to_cat)
                    .into(animGif)
                binding.catNonActiveGif.visibility = View.INVISIBLE
                binding.catInKitchenGif.visibility = View.INVISIBLE

                binding.backToCat.visibility = View.VISIBLE
                binding.backToCat.startAnimation(inAnimation)
                TimerGif = object : CountDownTimer(2000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        binding.backToCat.visibility = View.INVISIBLE
                        binding.dragBowl.isEnabled = true
                        onBackPressed()
                    }
                }
                TimerGif.start()
            }

        }
        attachViewDragListener()
        binding.dragBowl.setOnDragListener(maskDragListener)

    }

    override fun onBackPressed(){

        (applicationContext as App).isCreatingActivity = true
        super.onBackPressed()
    }

    fun animgif() {
        val imageView: ImageView = findViewById(R.id.cat_non_active_gif)
        Glide.with(this)
            .load(R.drawable.cat_non_active)
            .into(imageView)
    }

    fun ImageOnClick()
    {
        catFeed.start()
        binding.catInKitchenGif.setVisibility(View.VISIBLE)
        val imageView: ImageView = findViewById(R.id.cat_in_kitchen_gif)
        Glide.with(this)
            .load(R.drawable.cat_in_kitchen_active)
            .into(imageView)
        binding.catNonActiveGif.visibility = View.INVISIBLE
    }

    fun ResetImage()
    {
        if(catFeed.isPlaying) {
            catFeed.seekTo(0)
            catFeed.pause()
        }
        binding.catInKitchenGif.setVisibility(View.INVISIBLE)
        binding.catNonActiveGif.setVisibility(View.VISIBLE)
    }

    private val maskDragListener = View.OnDragListener { view, dragEvent ->

        val draggableItem = dragEvent.localState as View

        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                ImageOnClick()
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                binding.catNonActive.alpha = 0.3f
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                binding.catNonActive.alpha = 1.0f
                draggableItem.visibility = View.VISIBLE
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                binding.catNonActive.alpha = 1.0f
                if (dragEvent.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    val draggedData = dragEvent.clipData.getItemAt(0).text
                    println("draggedData $draggedData")
                }
                val parent = draggableItem.parent as ConstraintLayout
                parent.removeView(draggableItem)
                val dropArea = view as ConstraintLayout
                dropArea.addView(draggableItem)
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                ResetImage()
                draggableItem.visibility = View.VISIBLE
                view.invalidate()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun attachViewDragListener() {

        binding.dragBowl.setOnLongClickListener { view: View ->

            val item = ClipData.Item(maskDragMessage)
            val dataToDrag = ClipData(
                maskDragMessage,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )
            val maskShadow = MaskDragShadowBuilderBowl(view)
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
        if (!(applicationContext as App).isCreatingActivity && !(applicationContext as App).musicStart)
            (applicationContext as App).start1()
        (applicationContext as App).isCreatingActivity = false
        binding.dragBowl.isEnabled = false
            //TimerGif.cancel()
        val animGif: ImageView = findViewById(R.id.go_to_cat)
        Glide.with(this)
            .load(R.drawable.go_to_cat)
            .into(animGif)
        binding.catNonActiveGif.visibility = View.INVISIBLE
        binding.catInKitchenGif.visibility = View.INVISIBLE
        binding.goToCat.visibility = View.VISIBLE
        binding.goToCat.startAnimation(fromAnimation)
        TimerGif = object : CountDownTimer(2300, 2500) {
            override fun onTick(millisUntilFinished: Long) {
                if(binding.catNonActiveGif.visibility == View.VISIBLE) {
//                    binding.goToCat.clearAnimation()
//                    binding.goToCat.visibility = View.INVISIBLE
                }
            }
            override fun onFinish() {
                binding.goToCat.visibility = View.INVISIBLE
                binding.catNonActiveGif.visibility = View.VISIBLE
                binding.dragBowl.isEnabled = true
            }
        }
        TimerGif.start()
    }

}

private class MaskDragShadowBuilderBowl(view: View) : View.DragShadowBuilder(view) {

    private val shadow = ResourcesCompat.getDrawable(view.context.resources, R.drawable.bowl, view.context.theme)

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