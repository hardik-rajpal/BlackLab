package com.example.blacklab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void launchCustomViewsActivity(View view){
        Intent intent = new Intent(this, CustomViewsJava.class);
        startActivity(intent);
    }
    public void launchDisplayExpsActivity(View view) {
        Intent intent = new Intent(this,DisplayExperimentsActivity.class);
        startActivity(intent);
    }
    public void launchComposeExActivity(View view){
        Intent intent = new Intent(this, ComposeEx.class);
        startActivity(intent);
    }
    public void launchComposeAssistanceActivity(View view){
        Intent intent = new Intent(this, ComposeAssistance.class);
        startActivity(intent);
    }
    public void launchMultiViewActivity(View view){
        Intent intent = new Intent(this, ViewsVsActivitiesActivity.class);
        startActivity(intent);
    }
}