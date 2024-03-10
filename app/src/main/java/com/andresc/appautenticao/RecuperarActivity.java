package com.andresc.appautenticao;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.andresc.appautenticao.databinding.ActivityCadastroBinding;
import com.andresc.appautenticao.databinding.ActivityRecuperarBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RecuperarActivity extends AppCompatActivity {
    private ActivityRecuperarBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecuperarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.imgBtnBack.setOnClickListener(v -> finish());
        binding.btnRec.setOnClickListener(v -> validData());
    }
    private void validData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        String email = binding.editEmail.getText().toString().trim();

        if(email.isEmpty()){
            binding.editEmail.setError("Digite o Email");
            binding.editEmail.requestFocus();
            msgLog(1, "");
            binding.progressBar.setVisibility(View.INVISIBLE);
            }else{
                sendResetLink(email);
        }
    }

    private void sendResetLink(String emailAddress){
        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        msgLog(3, "");
                    } else{
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        String errMessage = Objects.requireNonNull(task.getException()).getMessage();
                        assert errMessage != null;
                        if(errMessage.equals("The email address is badly formatted.")){
                            binding.editEmail.setError("Email Inválido");
                            binding.editEmail.requestFocus();
                            msgLog(7, "");
                        }else{
                            msgLog(9,errMessage);
                        }
                    }
                });
    }

    private void msgLog(int log, String message){
        String[] msg = new String[10];
        msg[0] = "";
        msg[1] = "Email em branco";
        msg[2] = "Senha em branco";
        msg[3] = "Email enviado!";
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