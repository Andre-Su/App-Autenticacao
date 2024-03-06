package com.andresc.appautenticao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.andresc.appautenticao.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textCad.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });
        binding.textRec.setOnClickListener(v -> {
            startActivity(new Intent(this, RecuperarActivity.class));
        });

    }

}