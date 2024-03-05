package com.andresc.appautenticao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.andresc.appautenticao.databinding.ActivityCadastroBinding;
import com.andresc.appautenticao.databinding.ActivityRecuperarBinding;

public class RecuperarActivity extends AppCompatActivity {
    private ActivityRecuperarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecuperarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}