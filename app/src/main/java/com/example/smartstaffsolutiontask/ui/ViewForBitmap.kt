package com.example.smartstaffsolutiontask.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class ViewForBitmap @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0 ) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    var image: Bitmap? = null
        set(value) {
            field = value
            onPreChangeBitmap(image)
            onChangeBitmap(image)
        }

    var onBitmapClickListener: OnBitmapClickListener? = null

    private var isSurfaceCreated = false

    private var bitmapDrawThread: BitmapDrawThread? = null

    init {
        holder.addCallback(this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_UP -> {
                val X = event.x
                val Y = event.y

                if(X < 0 || Y < 0 || X > width || Y > height) return true

                val bitmapX = ((X / width) * (image?.width ?: 0)).toInt()
                val bitmapY = ((Y / height) * (image?.height ?: 0)).toInt()

                onBitmapClickListener?.onBitmapClicked(bitmapX, bitmapY)
            }
        }
        return true
    }

    private fun onChangeBitmap(image: Bitmap?) {
        if (image != null && isSurfaceCreated || image == null) startBitmapRendering()
    }

    private fun onPreChangeBitmap(image: Bitmap?) {
        if (bitmapDrawThread != null || image == null) stopBitmapRendering()
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        isSurfaceCreated = true
        if (image != null) startBitmapRendering()
    }

    private fun startBitmapRendering() {
        if(bitmapDrawThread != null) return
        bitmapDrawThread = BitmapDrawThread(holder, image!!)
        bitmapDrawThread!!.startDrawing()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) = Unit

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        isSurfaceCreated = false
        onSurfaceDestroyed()
    }

    private fun onSurfaceDestroyed() {
        stopBitmapRendering()
    }

    private fun stopBitmapRendering() {
        bitmapDrawThread?.stopDrawing()
        bitmapDrawThread = null
    }
}
