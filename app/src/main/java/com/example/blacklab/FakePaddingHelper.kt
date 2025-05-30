package com.example.blacklab

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Insets
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.core.view.children

object FakePaddingHelper {
    @RequiresApi(Build.VERSION_CODES.Q)
    @JvmStatic
    fun createFakeStripView(rootView: ViewGroup, viewToExtend: View, insets1: Insets, holder: FakeStripHolder) {
        val left = insets1.left
        // real padding
        rootView.setPadding(0, insets1.top, insets1.right, insets1.bottom)
        // fake padding
        // TODO left and right.
        if (left == 0) {
            return
        }
        rootView.post {
            val fakeStripView = getFakeStripView(viewToExtend, left)
            val params = ViewGroup.LayoutParams(left, viewToExtend.height)
            for (child in rootView.children.toList()) {
                if (child is FakeStripView) {
                    rootView.removeView(child)
                }
            }
            holder.setFakeStrip(fakeStripView)
            rootView.addView(
                fakeStripView,
                0,
                params
            )
        }
    }

    private fun getFakeStripView(viewToExtend: View, @Px stripWidth: Int): FakeStripView {
        val colors = getColors(viewToExtend)
        return FakeStripView(viewToExtend.context, colors, stripWidth)
    }
    @JvmStatic
    fun getColors(viewToExtend: View): IntArray {
        val colorStripsView: View = viewToExtend
        val bitmap = Bitmap.createBitmap(
            colorStripsView.width,
            colorStripsView.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        colorStripsView.draw(canvas)
        val colors = IntArray(colorStripsView.height)
        for (y in 0 until colorStripsView.height) {
            colors[y] = bitmap.getPixel(0, y)
        }
        return colors
    }

    @JvmStatic
    fun updateFakeStripView(mFakeStrip: FakeStripView, FirstContainer: View) {
        mFakeStrip
    }
}
interface FakeStripHolder {
    fun setFakeStrip(view: FakeStripView)
}