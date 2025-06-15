package com.example.electricitybillapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Goes back to MainActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Hide the ActionBar (black top bar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_about);

        // GitHub link button
        TextView link = findViewById(R.id.textLink);
        link.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Qasriena01")))
        );

        // Bottom navigation
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.nav_about); // Set About as selected

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Avoid back stack duplication
                startActivity(intent);
                overridePendingTransition(0, 0); // Optional: disable animation
                return true;
            } else if (id == R.id.nav_about) {
                return true;
            }

            return false;
        });
    }
}
