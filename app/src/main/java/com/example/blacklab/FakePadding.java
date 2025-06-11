package com.example.blacklab;

import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class FakePadding extends AppCompatActivity {

    private Insets mCachedInsets;
    private FakeStripView mFakeStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_padding);
        getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @NonNull
            @Override
            public WindowInsets onApplyWindowInsets(@NonNull View v, @NonNull WindowInsets insets) {
                Insets insets1 = insets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
                mCachedInsets = insets1;
                FakePaddingHelper.applyFakePadding(getRootview(), getFirstContainer(), insets1);
                return WindowInsets.CONSUMED;
            }
        });
    }

    private View getFirstContainer() {
        return findViewById(R.id.first_container);
    }

    private View getColorStripsView(){
        return findViewById(R.id.colorstrips);
    }
    private ViewGroup getRootview() {
        return this.findViewById(R.id.fakepaddingroot);
    }
}