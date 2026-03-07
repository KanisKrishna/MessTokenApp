package com.example.messtokenapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		TextView tvLogo = findViewById(R.id.tvLogo);
		TextView tvSubtitle = findViewById(R.id.tvSubtitle);

		Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

		tvLogo.startAnimation(zoomIn);
		tvSubtitle.startAnimation(zoomIn);

		new Handler().postDelayed(() -> {
			startActivity(new Intent(SplashActivity.this, HomeActivity.class));
			finish();
		}, 3000); // 3 seconds
	}
}