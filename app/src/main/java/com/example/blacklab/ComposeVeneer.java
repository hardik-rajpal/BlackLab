package com.example.blacklab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.compose.ui.platform.ComposeView;

/**
 * TODO: document your custom view class.
 */
public class ComposeVeneer extends FrameLayout {
    public ComposeVeneer(Context context, int layoutId){
        super(context);
        ComposeView composeView = new ComposeView(context);
        this.attachViewToParent(composeView,0, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

        LayoutInflater.from(context).inflate(layoutId,this);
    }
}