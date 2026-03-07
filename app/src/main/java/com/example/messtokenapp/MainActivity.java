package com.example.messtokenapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.material.button.MaterialButtonToggleGroup;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

	EditText etRoll;
	Button btnSubmit;
	TextView tvStatus;
	Button btnAdmin;
	Button btnBackToRoutines;
	TextView btnYear1, btnYear2, btnYear3, btnYear4;
	String selectedYear = "";

	FirebaseFirestore db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		etRoll = findViewById(R.id.etRoll);
		btnSubmit = findViewById(R.id.btnSubmit);
		tvStatus = findViewById(R.id.tvStatus);

		btnYear1 = findViewById(R.id.btnYear1);
		btnYear2 = findViewById(R.id.btnYear2);
		btnYear3 = findViewById(R.id.btnYear3);
		btnYear4 = findViewById(R.id.btnYear4);

		btnAdmin = findViewById(R.id.btnAdmin);
		btnBackToRoutines = findViewById(R.id.btnBackToRoutines);

		android.view.animation.Animation slideUp = android.view.animation.AnimationUtils.loadAnimation(this,
				R.anim.slide_up);
		etRoll.startAnimation(slideUp);
		findViewById(R.id.llYearSelector).startAnimation(slideUp);
		btnSubmit.startAnimation(slideUp);

		db = FirebaseFirestore.getInstance();

		String meal = getIntent().getStringExtra("meal");
		if (meal == null) {
			meal = "Unknown Meal";
		}

		final String finalMeal = meal;

		if (getSupportActionBar() != null) {
			getSupportActionBar().setTitle(finalMeal + " Token Desk");
		}

		btnBackToRoutines.setOnClickListener(v -> finish());

		android.view.View.OnClickListener yearListener = v -> {
			btnYear1.setBackgroundColor(getResources().getColor(R.color.colorSurface, getTheme()));
			btnYear1.setTextColor(getResources().getColor(R.color.colorOnSurface, getTheme()));
			btnYear2.setBackgroundColor(getResources().getColor(R.color.colorSurface, getTheme()));
			btnYear2.setTextColor(getResources().getColor(R.color.colorOnSurface, getTheme()));
			btnYear3.setBackgroundColor(getResources().getColor(R.color.colorSurface, getTheme()));
			btnYear3.setTextColor(getResources().getColor(R.color.colorOnSurface, getTheme()));
			btnYear4.setBackgroundColor(getResources().getColor(R.color.colorSurface, getTheme()));
			btnYear4.setTextColor(getResources().getColor(R.color.colorOnSurface, getTheme()));

			TextView clicked = (TextView) v;
			clicked.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getTheme()));
			clicked.setTextColor(getResources().getColor(R.color.white, getTheme()));

			if (v.getId() == R.id.btnYear1)
				selectedYear = "1";
			if (v.getId() == R.id.btnYear2)
				selectedYear = "2";
			if (v.getId() == R.id.btnYear3)
				selectedYear = "3";
			if (v.getId() == R.id.btnYear4)
				selectedYear = "4";
		};

		btnYear1.setOnClickListener(yearListener);
		btnYear2.setOnClickListener(yearListener);
		btnYear3.setOnClickListener(yearListener);
		btnYear4.setOnClickListener(yearListener);

		btnSubmit.setOnClickListener(v -> {
			tvStatus.setText("");
			tvStatus.setTextColor(getResources().getColor(R.color.colorPrimary, getTheme()));

			if (selectedYear.isEmpty()) {
				tvStatus.setTextColor(getResources().getColor(R.color.colorDestructive, getTheme()));
				tvStatus.setText("❌ Please select a Year first.");
				return;
			}

			final String finalYearStr = selectedYear;

			String roll = etRoll.getText().toString().trim().toUpperCase();

			if (roll.isEmpty()) {
				etRoll.setError("Enter Roll Number");
				return;
			}
			if (roll.length() != 4) {
				etRoll.setError("Must be exactly 4 characters (e.g. F224)");
				return;
			}

			String deptStr = roll.replaceAll("[0-9]", "");
			if (deptStr.isEmpty()) {
				deptStr = "GENERAL";
			}

			final String finalDeptStr = deptStr;

			tvStatus.setText("Verifying...");
			btnSubmit.setEnabled(false);

			db.collection("students").document("Year_" + finalYearStr).collection("department_" + finalDeptStr)
					.document(roll).get().addOnSuccessListener(documentSnapshot -> {
						if (documentSnapshot.exists()) {

							String todayDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
							String tokenId = todayDate + "_" + finalMeal + "_" + finalYearStr + "_" + finalDeptStr + "_"
									+ roll;

							db.collection("tokens").document(tokenId).get().addOnSuccessListener(tokenSnapshot -> {
								if (tokenSnapshot.exists()) {
									tvStatus.setTextColor(
											getResources().getColor(R.color.colorDestructive, getTheme()));
									tvStatus.setText("❌ Token Repeated! Already issued for " + finalMeal + ".");
									btnSubmit.setEnabled(true);
								} else {
									Map<String, Object> tokenData = new HashMap<>();
									tokenData.put("roll", roll);
									tokenData.put("year", finalYearStr);
									tokenData.put("dept", finalDeptStr);
									tokenData.put("meal", finalMeal);
									tokenData.put("date", todayDate);
									tokenData.put("timestamp", System.currentTimeMillis());

									db.collection("tokens").document(tokenId).set(tokenData)
											.addOnSuccessListener(aVoid -> {
												tvStatus.setTextColor(
														getResources().getColor(R.color.colorPrimary, getTheme()));
												tvStatus.setText(
														"✅ Token Given for " + roll + " (" + finalDeptStr + ")");
												etRoll.setText("");
												btnSubmit.setEnabled(true);
											}).addOnFailureListener(e -> {
												tvStatus.setTextColor(
														getResources().getColor(R.color.colorDestructive, getTheme()));
												tvStatus.setText("❌ Failed to save token.");
												btnSubmit.setEnabled(true);
											});
								}
							}).addOnFailureListener(e -> {
								tvStatus.setTextColor(getResources().getColor(R.color.colorDestructive, getTheme()));
								tvStatus.setText("❌ Connection Error.");
								btnSubmit.setEnabled(true);
							});

						} else {
							tvStatus.setTextColor(getResources().getColor(R.color.colorDestructive, getTheme()));
							tvStatus.setText("❌ " + roll + " (" + finalDeptStr + ") is not registered.");
							btnSubmit.setEnabled(true);
						}
					}).addOnFailureListener(e -> {
						tvStatus.setTextColor(getResources().getColor(R.color.colorDestructive, getTheme()));
						tvStatus.setText("❌ Database query failed.");
						btnSubmit.setEnabled(true);
					});
		});

		if (btnAdmin != null) {
			btnAdmin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AdminActivity.class)));
		}
	}
}