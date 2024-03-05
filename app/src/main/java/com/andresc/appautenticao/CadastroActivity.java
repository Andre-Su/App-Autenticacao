package com.andresc.appautenticao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.andresc.appautenticao.databinding.ActivityCadastroBinding;
import com.andresc.appautenticao.databinding.ActivityLoginBinding;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}