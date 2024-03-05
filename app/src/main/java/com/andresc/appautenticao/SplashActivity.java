package com.andresc.appautenticao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.andresc.appautenticao.databinding.ActivityLoginBinding;
import com.andresc.appautenticao.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler(getMainLooper()).postDelayed(()->{
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }, 3000);

    }
}