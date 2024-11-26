package com.example.blacklab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class EmojiKaleidoscope extends View {
    private String mStarterEmoji = "😍";
    // TODO: make this computed to maximize number of non-overlapping emojis.
    private Integer[] counts= {1, 4, 8, 16, 32};
    private List<TextPaint> mTextPaints;
    private List<String> emojis;
    private float mTextWidth;
    private float mTextHeight;
    private float angleOffset = 0;
    public EmojiKaleidoscope(Context context) {
        super(context);
        init(null, 0);
    }

    public EmojiKaleidoscope(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public EmojiKaleidoscope(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.EmojiKaleidoscope, defStyle, 0);

        mStarterEmoji = a.getString(
                R.styleable.EmojiKaleidoscope_startEmoji);

        a.recycle();

        // Set up a default TextPaint object
        mTextPaints = new ArrayList<TextPaint>();
        emojis = new ArrayList<>();
        emojis.add(mStarterEmoji);
        // Update TextPaint and text measurements from attributes
        // TODO: make it interactive later on:
        emojis.add("🤩");
        emojis.add("🪵");
        emojis.add("🔥");
        emojis.add("👟");
        int len = emojis.size();
        while(len-->0){
            invalidateTextPaintAndMeasurements();
        }
        animateThis();
    }

    private void animateThis(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                EmojiKaleidoscope.this.animateThis();
            }
        },25);
        if(this.isShown()){
            angleOffset+=0.5;
            invalidateTextPaintAndMeasurements();
        }
    }
    private void invalidateTextPaintAndMeasurements() {
        TextPaint newTextPaint = new TextPaint();
        newTextPaint.setTextSize(100);
        mTextWidth = newTextPaint.measureText(mStarterEmoji);
        Paint.FontMetrics fontMetrics = newTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
        mTextPaints.add(newTextPaint);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.

        int contentWidth = getWidth();
        int contentHeight = getHeight();
        canvas.translate(contentWidth/2,contentHeight/2);

        // Draw the text.
        int ring = 0;
        for(String emoji:emojis){
            drawEmojiInRing(emoji,canvas, contentWidth, contentHeight, ring);

            ring++;
        }

        // Draw the example drawable on top of the text.

    }

    private void drawEmojiInRing(String emoji, Canvas canvas, int contentWidth, int contentHeight, int ring) {
        if(ring >= counts.length || ring >= mTextPaints.size()){
            Log.e(VIEW_LOG_TAG,"invalid lengths detected");
            return;
        }
        int count = counts[ring];
        TextPaint paint = mTextPaints.get(ring);
        float angleDelta = 360/count;
        canvas.rotate(-angleOffset);
        canvas.save();
        for(int i=0;i<count;i++){
            canvas.rotate(-(angleDelta));
            float x = ((-mTextWidth) / 2) - ring*mTextWidth;
            float y = (mTextHeight) / 2;
            canvas.drawText(emoji,
                    x,
                    y,
                    paint);

        }
        canvas.restore();

    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mStarterEmoji;
    }

    /**
     * Sets the view"s example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mStarterEmoji = exampleString;
        invalidateTextPaintAndMeasurements();
    }


}