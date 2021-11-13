package com.example.smartstaffsolutiontask.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.SurfaceHolder
import java.util.concurrent.atomic.AtomicBoolean

class BitmapDrawThread(
        private val surfaceHolder: SurfaceHolder, private val image: Bitmap): Thread() {

    companion object {
        private const val DELAY_MS = 16
    }
    private var time = System.currentTimeMillis()

    private var flagRunning: AtomicBoolean = AtomicBoolean(false)
    var isRunning
        get() = flagRunning.get()
        set(value) = flagRunning.set(value)

    fun startDrawing() {
        isRunning = true
        super.start()
    }

    fun stopDrawing() {
        var retry = true
        isRunning = false
        while (retry){
            try {
                join()
                retry = false
            } catch (e: InterruptedException) {

            }
        }
    }

    override fun run() {
        super.run()
        var canvas: Canvas?
        while (isRunning) {
            val now = System.currentTimeMillis()
            val elapsedTime = now - time
            canvas = null
            if(elapsedTime > DELAY_MS) {
                time = now
                try {
                    canvas = surfaceHolder.lockCanvas(null)
                    if(canvas == null)
                        return
                    synchronized(surfaceHolder) {
                        canvas.drawColor(Color.BLACK)
                        val dest = Rect(0, 0, canvas.width - 1, canvas.height - 1)
                        synchronized(image){
                            val src = Rect(0, 0, image.width - 1, image.height - 1)
                            canvas.drawBitmap(image, src, dest, null)
                        }
                    }
                } finally {
                    if(canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    }
                }
            }
        }
    }
}
