package com.andresc.appautenticao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.andresc.appautenticao.databinding.ActivityLoginBinding;
import com.andresc.appautenticao.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.btnLogOut.setOnClickListener(v->{
            mAuth.signOut();
            Toast.makeText(this,"User - Sign out",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }else{
            Toast.makeText(MainActivity.this,"User - Logged in",Toast.LENGTH_SHORT).show();
        }
    }
}