package com.example.messtokenapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TokenActivity extends AppCompatActivity {

	TextView tvGeneratedTokenInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_token);

		tvGeneratedTokenInfo = findViewById(R.id.tvGeneratedTokenInfo);

		android.view.animation.Animation slideUp = android.view.animation.AnimationUtils.loadAnimation(this,
				R.anim.slide_up);
		findViewById(R.id.ticketCard).startAnimation(slideUp);

		// Receive data from MainActivity
		String roll = getIntent().getStringExtra("roll");
		String meal = getIntent().getStringExtra("meal");

		if (roll != null && meal != null) {
			String dateFormatted = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());

			String receiptText = "Date: " + dateFormatted + "\n" + "Meal: " + meal + "\n" + "Roll No: " + roll;

			tvGeneratedTokenInfo.setText(receiptText);
		} else {
			tvGeneratedTokenInfo.setText("Invalid Token Payload");
		}
	}
}