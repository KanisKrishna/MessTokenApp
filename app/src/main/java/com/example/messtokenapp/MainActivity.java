package com.example.messtokenapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etRoll;
    Button btnSubmit;
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etRoll = findViewById(R.id.etRoll);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvStatus = findViewById(R.id.tvStatus);

        String meal = getIntent().getStringExtra("meal");

        btnSubmit.setOnClickListener(v -> {
            String roll = etRoll.getText().toString().trim();

            if (roll.isEmpty()) {
                Toast.makeText(this, "Enter Roll Number", Toast.LENGTH_SHORT).show();
                return;
            }

            tvStatus.setText("Token Given for " + meal + " to Roll No: " + roll);
        });
    }
}