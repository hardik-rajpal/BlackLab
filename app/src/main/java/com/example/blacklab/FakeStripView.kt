package com.example.blacklab

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class FakeStripView(context: Context, var colors: IntArray, val stripWidth: Int) : View(context) {
    fun updateColors(_colors: IntArray){
        colors = _colors
        invalidate()
    }
    override fun onDraw(canvas: Canvas) {
        var startY = 0
        var currentColor = colors[0]
        val paint = Paint()
        for (y in colors.indices) {
            if (colors[y] != currentColor) {
                paint.color = currentColor
                canvas.drawRect(
                    0f,
                    startY.toFloat(),
                    stripWidth.toFloat(),
                    y.toFloat(),
                    paint
                )
                startY = y
                currentColor = colors[y]
            }
        }
        paint.color = currentColor
        canvas.drawRect(
            0f,
            startY.toFloat(),
            stripWidth.toFloat(),
            colors.size.toFloat(),
            paint
        )
    }
}
