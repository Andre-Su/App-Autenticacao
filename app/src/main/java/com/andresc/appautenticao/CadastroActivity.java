package com.andresc.appautenticao;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andresc.appautenticao.databinding.ActivityCadastroBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();



        binding.btnCads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {validData();}
        });
    }

    private void validData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        //coletar strings
        String email = binding.editEmail.getText().toString().trim();
        String passw = binding.editPassword.getText().toString().trim();

        if(email.isEmpty()){
            //email vazio
            binding.editEmail.setError("Informe o email");
            msgLog(1);
            Toast.makeText(this, "Email inválido!", Toast.LENGTH_SHORT).show();
            binding.editEmail.requestFocus();
            binding.progressBar.setVisibility(View.INVISIBLE);
        }else{
            if(passw.isEmpty()){
                //senha vazia
                binding.editPassword.setError("Informe a senha");
                msgLog(2);
                binding.editPassword.requestFocus();
                binding.progressBar.setVisibility(View.INVISIBLE);
            } else if (passw.length()<8) {
                //senha vazia
                binding.editPassword.setError("Menos de 8 dígitos!");
                msgLog(5);
                binding.editPassword.requestFocus();
                binding.progressBar.setVisibility(View.INVISIBLE);
            }else{
                //dados validados
                //solicitando criação da conta
                submitAccountFireBase(email, passw);
            }
        }
    }

    private void submitAccountFireBase(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        msgLog(3);
                        Toast.makeText(this, "Criado com sucesso!", Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.INVISIBLE);
                    }else{
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        msgLog(4);
                        Toast.makeText(this, "Ocorreu um erro na criação\nTente Novamente", Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void msgLog(int log){
        String[] msg = new String[6];
        msg[0] = "";
        msg[1] = "Email em branco";
        msg[2] = "Senha em branco";
        msg[3] = "Criado com Sucesso";
        msg[4] = "Ocorreu um erro durante a criação...\nTente Novamente";
        msg[5] = "Senha com menos de 8 dígitos!";

        switch (log){
            case 1:
                binding.textMsgLog.setText(msg[1]);
                break;
            case 2:
                binding.textMsgLog.setText(msg[2]);
                break;
            case 3:
                binding.textMsgLog.setText(msg[3]);
                break;
            case 4:
                binding.textMsgLog.setText(msg[4]);
                break;
            case 5:
                binding.textMsgLog.setText(msg[5]);
                break;
        }
        binding.textMsgLog.setVisibility(View.VISIBLE);
        new Handler(getMainLooper()).postDelayed(()->{
            binding.textMsgLog.setVisibility(View.GONE);
            binding.textMsgLog.setText(msg[0]);
        }, 5000);
    }
}