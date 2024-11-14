package com.example.blacklab;

import android.app.UiModeManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

enum ThemeSetting {
    Light, Dark, System
}

public class DisplayExperimentsActivity extends AppCompatActivity {

    UiModeManager uiModeManager;
    static ThemeSetting themeSetting = ThemeSetting.System;
    Boolean responsiveToSystemTheme = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        uiModeManager = (UiModeManager) (getSystemService(UI_MODE_SERVICE));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_experiments);
        updateTextView();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if (themeSetting == ThemeSetting.System) {
            applySystemTheme(newConfig);
            updateTextView();
            super.onConfigurationChanged(newConfig);
        }
        recreate();
    }

    public void toggleTheme(View view) {
        // toggle.
        toggleThemeSetting();
        applyThemeSetting();
        updateTextView();
    }

    private static void toggleThemeSetting() {
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
                if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
                    themeNameView.setText(R.string.theme_system_mode_dark);
                } else {
                    themeNameView.setText(R.string.theme_system_mode_light);
                }
                break;
        }
    }

    private void applySystemTheme(Configuration config) {
        if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
