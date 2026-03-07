package com.example.messtokenapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AdminActivity extends AppCompatActivity {

	EditText etRollAdmin, etYear;
	Button btnAddStudent, btnDeleteStudent, btnImportCsv;
	TextView tvAdminStatus;

	FirebaseFirestore db;

	private final ActivityResultLauncher<Intent> csvPickerLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(), result -> {
				if (result.getResultCode() == RESULT_OK && result.getData() != null) {
					Uri uri = result.getData().getData();
					if (uri != null) {
						parseAndUploadCsv(uri);
					}
				}
			});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);

		if (getSupportActionBar() != null) {
			getSupportActionBar().setTitle("Admin Dashboard");
		}

		etRollAdmin = findViewById(R.id.etRollAdmin);
		etYear = findViewById(R.id.etYear);
		btnAddStudent = findViewById(R.id.btnAddStudent);
		btnDeleteStudent = findViewById(R.id.btnDeleteStudent);
		btnImportCsv = findViewById(R.id.btnImportCsv);
		tvAdminStatus = findViewById(R.id.tvAdminStatus);

		db = FirebaseFirestore.getInstance();

		android.view.animation.Animation slideUp = android.view.animation.AnimationUtils.loadAnimation(this,
				R.anim.slide_up);
		etRollAdmin.startAnimation(slideUp);
		etYear.startAnimation(slideUp);
		btnAddStudent.startAnimation(slideUp);
		btnDeleteStudent.startAnimation(slideUp);
		btnImportCsv.startAnimation(slideUp);

		btnAddStudent.setOnClickListener(v -> {
			String roll = etRollAdmin.getText().toString().trim().toUpperCase();
			String year = etYear.getText().toString().trim();

			if (roll.isEmpty()) {
				etRollAdmin.setError("Roll required");
				return;
			}
			if (roll.length() != 4) {
				etRollAdmin.setError("Must be exactly 4 chars (e.g. F224)");
				return;
			}
			if (year.isEmpty()) {
				etYear.setError("Year required");
				return;
			}

			String deptStr = roll.replaceAll("[0-9]", "");
			if (deptStr.isEmpty()) {
				deptStr = "GENERAL";
			}

			final String finalDeptStr = deptStr;

			db.collection("students").document("Year_" + year).collection("department_" + finalDeptStr).document(roll)
					.set(new StudentModel(roll, finalDeptStr, year))
					.addOnSuccessListener(aVoid -> tvAdminStatus
							.setText("Student " + roll + " (Year " + year + " " + finalDeptStr + ") Added!"))
					.addOnFailureListener(e -> tvAdminStatus.setText("Failed to add student."));
		});

		btnDeleteStudent.setOnClickListener(v -> {
			String roll = etRollAdmin.getText().toString().trim().toUpperCase();
			String year = etYear.getText().toString().trim();

			if (roll.isEmpty()) {
				etRollAdmin.setError("Roll required");
				return;
			}
			if (roll.length() != 4) {
				etRollAdmin.setError("Must be exactly 4 chars (e.g. F224)");
				return;
			}
			if (year.isEmpty()) {
				etYear.setError("Year required");
				return;
			}

			String deptStr = roll.replaceAll("[0-9]", "");
			if (deptStr.isEmpty()) {
				deptStr = "GENERAL";
			}

			final String finalDeptStr = deptStr;

			db.collection("students").document("Year_" + year).collection("department_" + finalDeptStr).document(roll)
					.delete()
					.addOnSuccessListener(aVoid -> tvAdminStatus
							.setText("Student " + roll + " (Year " + year + " " + finalDeptStr + ") Deleted!"))
					.addOnFailureListener(e -> tvAdminStatus.setText("Failed to delete."));
		});

		btnImportCsv.setOnClickListener(v -> {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			// Allow any file since some CSVs don't register strict mime types consistently
			intent.setType("*/*");
			intent.putExtra(Intent.EXTRA_MIME_TYPES,
					new String[] { "text/csv", "text/comma-separated-values", "application/csv" });
			intent.addCategory(Intent.CATEGORY_OPENABLE);

			try {
				csvPickerLauncher.launch(Intent.createChooser(intent, "Select CSV File"));
			} catch (Exception e) {
				Toast.makeText(this, "No file manager found.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void parseAndUploadCsv(Uri uri) {
		tvAdminStatus.setText("Reading CSV File...");
		int successCount = 0;
		int failCount = 0;

		try (InputStream inputStream = getContentResolver().openInputStream(uri);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

			String line;
			boolean isFirstLine = true;

			while ((line = reader.readLine()) != null) {
				// Skip header line if it looks like one (e.g., contains "roll" or "number")
				if (isFirstLine) {
					isFirstLine = false;
					if (line.toLowerCase().contains("roll") || line.toLowerCase().contains("name")) {
						continue;
					}
				}

				String[] columns = line.split(",");
				if (columns.length >= 2) {
					String roll = columns[0].trim().toUpperCase();
					String year = columns[1].trim();

					if (!roll.isEmpty() && roll.length() == 4 && !year.isEmpty()) {
						String deptStr = roll.replaceAll("[0-9]", "");
						if (deptStr.isEmpty()) {
							deptStr = "GENERAL";
						}
						db.collection("students").document("Year_" + year).collection("department_" + deptStr)
								.document(roll).set(new StudentModel(roll, deptStr, year));
						successCount++;
					} else {
						failCount++;
					}
				}
			}
			tvAdminStatus.setText("CSV Import Complete!\nImported: " + successCount + "\nSkipped/Failed: " + failCount);

		} catch (Exception e) {
			e.printStackTrace();
			tvAdminStatus.setText("Error parsing file: " + e.getMessage());
		}
	}
}