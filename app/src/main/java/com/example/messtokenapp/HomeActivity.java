package com.example.messtokenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class HomeActivity extends AppCompatActivity {

	Button btnTokenTab, btnAdminTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		btnTokenTab = findViewById(R.id.btnTokenTab);
		btnAdminTab = findViewById(R.id.btnAdminTab);

		Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
		Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

		findViewById(R.id.tvWelcome).startAnimation(fadeIn);
		findViewById(R.id.tvSubtitle).startAnimation(fadeIn);
		findViewById(R.id.cardToken).startAnimation(slideUp);
		findViewById(R.id.cardAdmin).startAnimation(slideUp);

		btnTokenTab.setOnClickListener(v -> {
			startActivity(new Intent(HomeActivity.this, MealSelectActivity.class));
		});

		btnAdminTab.setOnClickListener(v -> {
			startActivity(new Intent(HomeActivity.this, AdminActivity.class));
		});
	}
}