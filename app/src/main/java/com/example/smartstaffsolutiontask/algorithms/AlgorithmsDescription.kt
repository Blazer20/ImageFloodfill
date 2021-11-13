package com.example.smartstaffsolutiontask.algorithms

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.os.Parcel
import androidx.core.graphics.get
import androidx.core.graphics.set
import java.util.*

sealed class AlgorithmsDescription(val image: Bitmap, val startPoint: Point, val color: Int) {

    companion object {
        fun create(image: Bitmap, startPoint: Point, color: Int, algorithm: Algorithms) =
                when (algorithm) {
                    Algorithms.QUEUE -> Queue(image, startPoint, color)
                    Algorithms.STACK -> Stack(image, startPoint, color)
                    Algorithms.SCANLINE -> Scanline(image, startPoint, color)
                }

        fun create(image: Bitmap, parcel: Parcel): AlgorithmsDescription {
            val algorithm = parcel.readEnum<Algorithms>()!!
            val startPoint = parcel.readParcelable<Point>(Point::class.java.classLoader)!!
            val color = parcel.readInt()
            val startColor = parcel.readInt()
            val currentPoint = parcel.readParcelable<Point>(Point::class.java.classLoader)!!
            val points = parcel.createTypedArray(Point.CREATOR)!!

            return create(image, startPoint, color, algorithm).apply {
                this.startColor = startColor
                this.currentPoint = currentPoint
                this.points.addAll(points)
            }
        }

    }

    internal var currentPoint = startPoint
    internal var startColor = image[startPoint.x, startPoint.y]

    internal val points: ArrayDeque<Point> = ArrayDeque()

    val isDone
        get() = points.isEmpty()

    abstract fun step()

    fun fill() {
        while (!isDone)
            step()
    }

    private val algorithm: Algorithms
        get() = when (this) {
            is Scanline -> Algorithms.SCANLINE
            is Stack -> Algorithms.STACK
            is Queue -> Algorithms.QUEUE
        }

    fun writeToParcel(parcel: Parcel) {
        parcel.writeEnum(algorithm)
        parcel.writeParcelable(startPoint, 0)
        parcel.writeInt(color)
        parcel.writeInt(startColor)
        parcel.writeParcelable(currentPoint, 0)
        parcel.writeTypedArray(points.toTypedArray(), 0)
    }

    class Stack(image: Bitmap, startPoint: Point, color: Int) :
        AlgorithmsDescription(image, startPoint, color) {

        init {
            points.push(startPoint)
        }

        override fun step() {
            if (points.isEmpty())
                return

            currentPoint = points.pop()
            val x = currentPoint.x
            val y = currentPoint.y

            if (x < 0 || y < 0 || x >= image.width || y >= image.height)
                return
            if (image[x, y] != startColor || image[x, y] == color)
                return

            image[x, y] = color

            points.push(Point(x - 1, y))
            points.push(Point(x + 1, y))
            points.push(Point(x, y - 1))
            points.push(Point(x, y + 1))
        }
    }

    class Queue(image: Bitmap, startPoint: Point, color: Int) :
            AlgorithmsDescription(image, startPoint, color) {

        init {
            points.offer(startPoint)
        }

        override fun step() {
            if (points.isEmpty())
                return

            currentPoint = points.poll()!!
            val x = currentPoint.x
            val y = currentPoint.y

            if (x < 0 || y < 0 || x >= image.width || y >= image.height)
                return
            if (image[x, y] != startColor || image[x, y] == color)
                return

            image[x, y] = color

            points.offer(Point(x - 1, y))
            points.offer(Point(x + 1, y))
            points.offer(Point(x, y - 1))
            points.offer(Point(x, y + 1))
        }
    }

    class Scanline(image: Bitmap, startPoint: Point, color: Int) :
            AlgorithmsDescription(image, startPoint, color) {
        private val canvas = Canvas(image)
        private val paint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.0f
            strokeCap = Paint.Cap.BUTT
            strokeJoin = Paint.Join.BEVEL
            strokeMiter = 0.0f
            isAntiAlias = false
            setColor(color)
        }

        init {
            if (image[startPoint.x, startPoint.y] != color)
                points.push(startPoint)
        }

        override fun step() {
            if (points.isEmpty())
                return

            currentPoint = points.pop()
            val x = currentPoint.x
            val y = currentPoint.y

            val startX = getLeftX(x, y)
            val endX = getRightX(x, y)

            addSeeds(startX, endX, y + 1)
            addSeeds(startX, endX, y - 1)

            canvas.drawLine(
                    startX.toFloat(),
                    y.toFloat(),
                    endX.toFloat() + 1,
                    y.toFloat(),
                    paint
            )
        }

        private fun getLeftX(x: Int, y: Int): Int {
            var leftX = x
            while (leftX >= 0 && image[leftX, y] == startColor) {
                leftX--
            }
            return leftX + 1
        }

        private fun getRightX(x: Int, y: Int): Int {
            var rightX = x
            while (rightX < image.width && image[rightX, y] == startColor) {
                rightX++
            }
            return rightX - 1
        }

        private fun addSeeds(start: Int, end: Int, y: Int) {

            if (y < 0 || y >= image.height)
                return

            var isNextValidPointIsSeed = true
            for (i in start..end) {
                if (image[i, y] == startColor) {
                    if (isNextValidPointIsSeed) {
                        isNextValidPointIsSeed = false
                        points.push(Point(i, y))
                    }
                } else {
                    isNextValidPointIsSeed = true
                }
            }
        }
    }


}

inline fun <reified T : Enum<T>> Parcel.readEnum() =
        readString()?.let { enumValueOf<T>(it) }

inline fun <T : Enum<T>> Parcel.writeEnum(value: T?) =
        writeString(value?.name)