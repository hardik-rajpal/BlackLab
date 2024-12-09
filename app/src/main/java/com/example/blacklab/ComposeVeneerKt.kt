package com.example.blacklab

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.viewinterop.AndroidView

class ComposeVeneerKt(context:Context, layoutId:Int):FrameLayout(context) {
    init {
        val composeView = ComposeView(context);
        this.addView(composeView)
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // compose world goes here.
                // TODO: state variable here.
                AndroidView(factory = { ctx ->
                    ComposeVeneer(ctx,layoutId);
                })
            }
        }
    }
}


@Composable
fun Theme(darkMode:Boolean = isSystemInDarkTheme(), content:@Composable ()->Unit){

    content()
}