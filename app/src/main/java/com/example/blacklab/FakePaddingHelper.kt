package com.example.blacklab

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Insets
import android.graphics.Paint
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.core.view.children

object FakePaddingHelper {
    @RequiresApi(Build.VERSION_CODES.Q)
    @JvmStatic
    fun applyFakePadding(rootView: ViewGroup, viewToExtend:View, padding: android.graphics.Insets) {
        val left = padding.left
        val right = padding.right;
        // real padding
        rootView.setPadding(0, padding.top, 0, padding.bottom)
        if (left + right == 0) {
            for(child in rootView.children.toList()){
                if (child is FakeStripView) {
                    rootView.removeView(child)
                }
            }
            return
        }
        // fake padding
        rootView.post {
            var fakeStripExists = false
            for(child in rootView.children.toList()){
                if (child is FakeStripView) {
                    fakeStripExists = true
                    child.updateColorsAndWidth(padding)
                }
            }
            if (!fakeStripExists) {
                val paramsLeft = ViewGroup.LayoutParams(left, viewToExtend.height)
                val paramsRight = ViewGroup.LayoutParams(right, viewToExtend.height)
                val fakeStripViewLeft = getFakeStripView(viewToExtend, left, isLeft = true)
                val fakeStripViewRight = getFakeStripView(viewToExtend, right, isLeft = false)
                rootView.addView(
                    fakeStripViewLeft,
                    0,
                    paramsLeft
                )
                rootView.addView(fakeStripViewRight, -1, paramsRight)
                rootView.viewTreeObserver.addOnPreDrawListener {
                    for(child in rootView.children.toList()){
                        if (child is FakeStripView) {
                            child.updateColorsAndWidth(null)
                        }
                    }
                    true
                }
            }

        }
    }

    private fun getFakeStripView(viewToExtend: View, @Px stripWidth: Int, isLeft: Boolean): View {
        val colors = getColorArray(viewToExtend, isLeft)
        return FakeStripView(viewToExtend, colors, stripWidth, isLeft)
    }

    fun getColorArray(
        viewToExtend: View,
        isLeft: Boolean
    ): IntArray {
        val colorStripsView: View = viewToExtend
        val observedPixelsWidth = 1
        val viewHeight = colorStripsView.height
        val bitmap = Bitmap.createBitmap(
            /*width*/observedPixelsWidth,
            viewHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        colorStripsView.draw(canvas)
        if (!isLeft) {
            // for the right strip, translate the canvas
            // so the rightmost pixels come into the bitmap
            val dx = -(colorStripsView.width - observedPixelsWidth).toFloat()
            val dy = 0f
            canvas.translate(dx, dy)
            // clip so only the line of rightmost pixels is drawn
            canvas.clipRect(0, 0, observedPixelsWidth, viewHeight)
        }
        val colors = IntArray(viewHeight)
        val startX = 0
        for (y in 0 until viewHeight) {
            colors[y] = bitmap.getPixel(startX, y)
        }
        bitmap.recycle()
        return colors
    }
}

class FakeStripView(val viewToExtend: View, var colors: IntArray, var stripWidth: Int, val isLeft: Boolean) : View(viewToExtend.context) {
    fun updateColorsAndWidth(padding: android.graphics.Insets?){
        var mustInvalidate = false
        if (padding != null) {
            val newWidth = if (isLeft) padding.left else padding.right
            if (newWidth != stripWidth) {
                stripWidth = newWidth
                mustInvalidate = true
            }
            else {
                if (stripWidth == 0) {
                    return
                }
            }
        }
        val newColors = FakePaddingHelper.getColorArray(viewToExtend, isLeft)
        if (!newColors.contentEquals(colors)) {
            colors = newColors
            mustInvalidate = true
        }
        if (mustInvalidate) {
            invalidate()
        }
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
