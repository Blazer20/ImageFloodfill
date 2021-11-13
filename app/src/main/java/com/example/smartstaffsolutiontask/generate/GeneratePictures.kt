package com.example.smartstaffsolutiontask.generate

import android.graphics.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.example.smartstaffsolutiontask.algorithms.AlgorithmsDescription
import kotlin.math.max
import kotlin.math.sqrt
import kotlin.random.Random

class GeneratePictures {

    @RequiresApi(Build.VERSION_CODES.O)
    fun generate(width: Int, height: Int, seed: Long = (System.currentTimeMillis() + (width and height))): Bitmap {
        val random = Random(seed)
        val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565, false)
        for (i in 0 until width) {
            for (j in 0 until height) {
                val nextBool = random.nextBoolean()
                bitmap[i, j] = if (nextBool) Color.WHITE else Color.BLACK
            }
        }

        drawCircle(width, height, random, bitmap)

        return bitmap
    }

    private fun drawCircle(width: Int, height: Int, random: Random, bitmap: Bitmap) {
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.BLACK
        paint.strokeWidth = 0.9f
        paint.strokeJoin = Paint.Join.MITER
        paint.strokeMiter = 0.1f
        paint.strokeCap = Paint.Cap.ROUND
        paint.isAntiAlias = false

        val size = random.nextInt(5, max(6, width / 8)).toFloat()

        val circleY = random.nextInt((size / 2).toInt(), max((size / 2).toInt(), height)).toFloat()
        val circleX = random.nextInt((size / 2).toInt(), max((size / 2).toInt(), width)).toFloat()


        canvas.drawCircle(circleX + size / 4, circleY - size / 4, size * 2f, paint)
        paint.color = Color.BLACK
        val path = Path()
        path.moveTo(circleX + size, circleY - size)
        path.lineTo(circleX, circleY + size)
        path.moveTo(circleX, circleY + size)
        path.lineTo(circleX - size, circleY)
        path.moveTo(circleX - size, circleY)
        path.lineTo(circleX + size, circleY - size)
        path.close()
        canvas.drawPath(path, paint)
        canvas.drawLine(circleX + size - 1, circleY - size + 1, circleX, circleY + size, paint)

        AlgorithmsDescription.Scanline(bitmap, Point(circleX.toInt(), circleY.toInt()), Color.BLACK).fill()

        paint.color = Color.BLACK

        val radius = sqrt(2 * size * size) / 2.0f
        canvas.drawCircle((2 * circleX - size) / 2, (2 * circleY + size) / 2, radius, paint)
    }
}