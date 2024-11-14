package com.example.blacklab;

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

enum ThemeSetting{
    Light,Dark,System
}
public class DisplayExperimentsActivity extends AppCompatActivity {
    ThemeSetting themeSetting = ThemeSetting.Light;
    Boolean responsiveToSystemTheme = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_experiments);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(themeSetting==ThemeSetting.System){
            TextView themeNameView = findViewById(R.id.text_view_theme_name);
            applySystemTheme(newConfig,themeNameView);
        }
    }

    public void toggleTheme(View view) {
        TextView themeNameView = findViewById(R.id.text_view_theme_name);
        // toggle.
        switch (themeSetting){
            case Light:
                themeSetting = ThemeSetting.Dark;
                break;
            case Dark:
                themeSetting = ThemeSetting.System;
                break;
            case System:
                themeSetting = ThemeSetting.Light;
                break;
        }
        // update display theme post toggle.
        switch (themeSetting){
            case Light:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                themeNameView.setText(R.string.theme_light_mode);
                break;
            case Dark:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                themeNameView.setText(R.string.theme_dark_mode);
                break;
            case System:
                Configuration config = getResources().getConfiguration();
                applySystemTheme(config, themeNameView);
                break;
        }

    }

    private static void applySystemTheme(Configuration config, TextView themeNameView) {
        if((config.uiMode & config.UI_MODE_NIGHT_MASK) == config.UI_MODE_NIGHT_YES){
            // night mode active. Using this to permit lower API versions (<30)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            themeNameView.setText(R.string.theme_system_mode_dark);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            themeNameView.setText(R.string.theme_system_mode_light);
        }
    }
}
