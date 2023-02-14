package com.example.pusheencatsimulator

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.constraint.ConstraintLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.example.pusheencatsimulator.databinding.ActivityBathroomBinding


class BathroomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBathroomBinding
    private val maskDragMessage = ""
    private lateinit var catShower: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBathroomBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        catShower = MediaPlayer.create(this, R.raw.shower)

        animgif()
        binding.backToMain.setOnClickListener {
            onBackPressed()
        }
        attachViewDragListener()
        binding.dragSoap.setOnDragListener(maskDragListener)

    }

    fun animgif() {
        val imageView: ImageView = findViewById(R.id.cat_non_active_gif)
        Glide.with(this)
            .load(R.drawable.cat_non_active)
            .into(imageView)
    }

    fun ImageOnClick()
    {
        catShower.start()
        binding.catInBathroomGif.setVisibility(View.VISIBLE)
        val imageView: ImageView = findViewById(R.id.cat_in_bathroom_gif)
        Glide.with(this)
            .load(R.drawable.cat_in_bathroom_active)
            .into(imageView)
        binding.catNonActiveGif.visibility = View.INVISIBLE
        binding.catInBathroomNonActive.setVisibility(View.INVISIBLE)
    }

    fun ResetImage()
    {
        if(catShower.isPlaying) {
            catShower.seekTo(0)
            catShower.pause()
        }
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

        //binding.catNonActiveGif.visibility = View.INVISIBLE
        //binding.catNonActive.visibility = View.VISIBLE

        binding.dragSoap.setOnLongClickListener { view: View ->

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