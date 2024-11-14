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
    static ThemeSetting themeSetting = ThemeSetting.Light;
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
        super.onConfigurationChanged(newConfig);
        if (themeSetting == ThemeSetting.System) {
            applySystemTheme(newConfig);
            updateTextView();
            recreate();
        }
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
                Configuration config = getResources().getConfiguration();
                applySystemTheme(config);
                break;
        }
        recreate();
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
                Configuration config = getResources().getConfiguration();
//                if((config.uiMode & config.UI_MODE_NIGHT_MASK) == config.UI_MODE_NIGHT_YES){
                // night mode active. Using this to permit lower API versions (<30)
                if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
                    themeNameView.setText(R.string.theme_system_mode_dark);
                } else {
                    themeNameView.setText(R.string.theme_system_mode_light);
                }
                break;
        }
    }

    private void applySystemTheme(Configuration config) {
//        if((config.uiMode & config.UI_MODE_NIGHT_MASK) == config.UI_MODE_NIGHT_YES){
        if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {

            // night mode active. Using this to permit lower API versions (<30)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
