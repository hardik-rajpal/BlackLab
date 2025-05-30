package com.example.blacklab;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

enum ThemeSetting {
    Light, Dark, System
}

public class DisplayExperimentsActivity extends AppCompatActivity {

    ThemeSetting themeSetting = ThemeSetting.System;
    static int mUiMode;
    private SharedPreferences sprefs;
    private SharedPreferences.Editor storage;
    Boolean responsiveToSystemTheme = true;
    private String SP_THEME_SETTING_KEY = "themeSetting";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_experiments);
        EdgeToEdge.enable(this);

        //        ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), new OnApplyWindowInsetsListener() {
//            @NonNull
//            @Override
//            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
//                androidx.core.graphics.Insets padding = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//                v.setPadding(padding.left, padding.top, padding.right, padding.bottom);
//                return insets;
//            }
//        });
        sprefs = getSharedPreferences("DisplayExperiments", MODE_PRIVATE);
        storage = sprefs.edit();
        themeSetting = fetchThemeSetting();
//        this.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.white)));
        updateTextView();
        applyThemeSetting();
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        boolean uiModeNeedsUpdate = false;
        boolean newUiModeIsDifferentFromCurrent = mUiMode != newConfig.uiMode;
        if (themeSetting == ThemeSetting.System) {
            if (newUiModeIsDifferentFromCurrent) {
                uiModeNeedsUpdate = true;
                updateTextView();
            }
        } else if (themeSetting == ThemeSetting.Dark) {
            if (newUiModeIsDifferentFromCurrent && isUiModeIndicativeOfDarkMode(newConfig.uiMode)) {
                uiModeNeedsUpdate = true;
            }
        } else {
            //ThemeSetting.Light
            if (newUiModeIsDifferentFromCurrent && !isUiModeIndicativeOfDarkMode(newConfig.uiMode)) {
                uiModeNeedsUpdate = true;
            }
        }

        // additionally, we need a startup call from ThemeManager filter.
        if (uiModeNeedsUpdate) {
            mUiMode = newConfig.uiMode;
            recreate();
        }


    }

    public void toggleTheme(View view) {
        // toggle.
        toggleThemeSetting();
        storeThemeSetting();
        applyThemeSetting();
        updateTextView();
    }

    private void storeThemeSetting() {
        storage.putInt(SP_THEME_SETTING_KEY, themeSetting.ordinal());
        storage.apply();
    }

    private ThemeSetting fetchThemeSetting() {
        int ordinal = sprefs.getInt(SP_THEME_SETTING_KEY, ThemeSetting.System.ordinal());
        return ThemeSetting.values()[ordinal];
    }

    private void toggleThemeSetting() {
        switch (themeSetting) {
            case Light:
                themeSetting = ThemeSetting.System;
                break;
            case Dark:
                themeSetting = ThemeSetting.Light;
                break;
            case System:
                themeSetting = ThemeSetting.Dark;
                break;
        }
    }

    private void applyThemeSetting() {
        if (isUiModeIndicativeOfDarkMode(getResources().getConfiguration().uiMode)) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                WindowInsets consumed = WindowInsets.CONSUMED;
                Insets insets = consumed.getInsets(WindowInsets.Type.navigationBars());

            } else {
                getWindow().setNavigationBarColor(Color.TRANSPARENT);
            }
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                WindowInsetsController controller = getWindow().getInsetsController();
                controller.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
            } else {
                getWindow().setNavigationBarColor(Color.TRANSPARENT);
            }
        }


        switch (themeSetting) {
            case Light:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Dark:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case System:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    private void updateTextView() {
        TextView themeNameView = findViewById(R.id.text_view_theme_name);
        switch (themeSetting) {
            case Light:
                themeNameView.setText(R.string.theme_light_mode);
                break;
            case Dark:
                themeNameView.setText(R.string.theme_dark_mode);
                break;
            case System:
                if (isSystemInDarkMode()) {
                    themeNameView.setText(R.string.theme_system_mode_dark);
                } else {
                    themeNameView.setText(R.string.theme_system_mode_light);
                }
                break;
        }
    }

    private boolean isSystemInDarkMode() {
        int uiMode = getBaseContext().getApplicationContext().getResources().getConfiguration().uiMode;
        return isUiModeIndicativeOfDarkMode(uiMode);
    }

    private static boolean isUiModeIndicativeOfDarkMode(int uiMode) {
        return (uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    private void applySystemTheme(Configuration config) {
        if (isSystemInDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
