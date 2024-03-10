package com.andresc.appautenticao;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.andresc.appautenticao.databinding.ActivityCadastroBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.btnCads.setOnClickListener(v -> validData());
        binding.imgBtnBack.setOnClickListener(v -> finish());
    }
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }

    private void validData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.editEmail.setError(null);
        binding.editPassword.setError(null);
        binding.btnCads.setClickable(false);

        String email = binding.editEmail.getText().toString().trim();
        String passw = binding.editPassword.getText().toString().trim();

        if(email.isEmpty()){
            binding.editEmail.setError("Digite o Email");
            binding.editEmail.requestFocus();
            msgLog(1, "");
            binding.progressBar.setVisibility(View.INVISIBLE);
        }else{
            if(passw.isEmpty()){
                binding.editPassword.setError("Digite a senha");
                binding.editPassword.requestFocus();
                msgLog(2, "");
                binding.progressBar.setVisibility(View.INVISIBLE);
            } else if (passw.length()<8) {
                binding.editPassword.setError("Menos de 8 dígitos");
                binding.editPassword.requestFocus();
                msgLog(5, "");
                binding.progressBar.setVisibility(View.INVISIBLE);
            }else{
                createAccountFirebase(email, passw);
            }
        }
        binding.btnCads.setClickable(true);
    }

    private void createAccountFirebase(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        currentUser = mAuth.getCurrentUser();
                        msgLog(3, "");
                        new Handler(getMainLooper()).postDelayed(this::finish, 4001);
                    }else{
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        String message = (Objects.requireNonNull(task.getException()).getMessage());
                        assert message != null;
                        if(message.equals("The email address is already in use by another account.")){
                            binding.editEmail.setError("Email já está em uso");
                            binding.editEmail.requestFocus();
                            msgLog(6, "");

                        } else if (message.equals("The email address is badly formatted.")) {
                            binding.editEmail.setError("Email Inválido");
                            binding.editEmail.requestFocus();
                            msgLog(7, "");
                        } else {
                            msgLog(9,message);
                        }
                    }
                });
    }

    private void msgLog(int log, String message){
        String[] msg = new String[10];
        msg[0] = "";
        msg[1] = "Email em branco";
        msg[2] = "Senha em branco";
        msg[3] = "Criado com Sucesso!\nRedirecionando para Login...";
        msg[4] = "Ocorreu um erro...\nTente Novamente";
        msg[5] = "Senha com menos de 8 dígitos!";
        msg[6] = "Este email já está sendo usado por outra conta";
        msg[7] = "Email digitado Incorretamente.";
        msg[8] = "--";

        if(!message.isEmpty()) msg[9] = message;
        else msg[9] = msg[4];

        switch (log){
            case 1: binding.textMsgLog.setText(msg[1]);break;
            case 2: binding.textMsgLog.setText(msg[2]);break;
            case 3: binding.textMsgLog.setText(msg[3]);break;
            case 4: binding.textMsgLog.setText(msg[4]);break;
            case 5: binding.textMsgLog.setText(msg[5]);break;
            case 6: binding.textMsgLog.setText(msg[6]);break;
            case 7: binding.textMsgLog.setText(msg[7]);break;
            case 8: binding.textMsgLog.setText(msg[8]);break;
            case 9: binding.textMsgLog.setText(msg[9]);break;
        }
        binding.textMsgLog.setVisibility(View.VISIBLE);
        new Handler(getMainLooper()).postDelayed(()->{
            binding.textMsgLog.setText(msg[0]);
            binding.textMsgLog.setVisibility(View.GONE);
        }, 4000);
    }
}