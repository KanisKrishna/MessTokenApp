package com.example.messtokenapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminActivity extends AppCompatActivity {

    EditText etRollAdmin, etDept, etYear;
    Button btnAddStudent, btnDeleteStudent;
    TextView tvAdminStatus;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        etRollAdmin = findViewById(R.id.etRollAdmin);
        etDept = findViewById(R.id.etDept);
        etYear = findViewById(R.id.etYear);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnDeleteStudent = findViewById(R.id.btnDeleteStudent);
        tvAdminStatus = findViewById(R.id.tvAdminStatus);

        db = FirebaseFirestore.getInstance();

        btnAddStudent.setOnClickListener(v -> {
            String roll = etRollAdmin.getText().toString();
            String dept = etDept.getText().toString();
            String year = etYear.getText().toString();

            db.collection("students").document(roll)
                    .set(new StudentModel(roll, dept, year));

            tvAdminStatus.setText("Student Added");
        });

        btnDeleteStudent.setOnClickListener(v -> {
            String roll = etRollAdmin.getText().toString();
            db.collection("students").document(roll).delete();
            tvAdminStatus.setText("Student Deleted");
        });
    }
}