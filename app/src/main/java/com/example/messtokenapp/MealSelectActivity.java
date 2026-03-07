package com.example.messtokenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MealSelectActivity extends AppCompatActivity {

	TextView btnBreakfast, btnLunch, btnSnacks, btnDinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meal_select);

		btnBreakfast = findViewById(R.id.btnBreakfast);
		btnLunch = findViewById(R.id.btnLunch);
		btnSnacks = findViewById(R.id.btnSnacks);
		btnDinner = findViewById(R.id.btnDinner);

		Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
		btnBreakfast.startAnimation(slideUp);
		btnLunch.startAnimation(slideUp);
		btnSnacks.startAnimation(slideUp);
		btnDinner.startAnimation(slideUp);

		View.OnClickListener listener = v -> {
			String meal = "";

			if (v.getId() == R.id.btnBreakfast)
				meal = "Breakfast";
			if (v.getId() == R.id.btnLunch)
				meal = "Lunch";
			if (v.getId() == R.id.btnSnacks)
				meal = "Snacks";
			if (v.getId() == R.id.btnDinner)
				meal = "Dinner";

			Intent i = new Intent(MealSelectActivity.this, MainActivity.class);
			i.putExtra("meal", meal);
			startActivity(i);
		};

		btnBreakfast.setOnClickListener(listener);
		btnLunch.setOnClickListener(listener);
		btnSnacks.setOnClickListener(listener);
		btnDinner.setOnClickListener(listener);
	}
}