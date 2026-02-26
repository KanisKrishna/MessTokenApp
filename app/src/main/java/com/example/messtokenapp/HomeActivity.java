package com.example.messtokenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button btnTokenTab, btnAdminTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnTokenTab = findViewById(R.id.btnTokenTab);
        btnAdminTab = findViewById(R.id.btnAdminTab);

        btnTokenTab.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MealSelectActivity.class));
        });

        btnAdminTab.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AdminActivity.class));
        });
    }
}