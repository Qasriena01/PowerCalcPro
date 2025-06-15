package com.example.electricitybillapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the ActionBar (removes the black bar with title)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Make full screen (hides status bar too)
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_welcome);

        // Delay 3 seconds before navigating to MainActivity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish(); // Prevent back navigation to splash
        }, 3000);
    }
}
