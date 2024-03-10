package com.andresc.appautenticao;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andresc.appautenticao.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(v -> validData());

        binding.textCad.setOnClickListener(v -> startActivity(new Intent(this, CadastroActivity.class)));
        binding.textRec.setOnClickListener(v -> startActivity(new Intent(this, RecuperarActivity.class)));

    }
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.d(TAG, "Firebase User is Logged-in");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void validData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.editEmail.setError(null);
        binding.editPassword.setError(null);
        binding.btnLogin.setClickable(false);

        String email = binding.editEmail.getText().toString().trim();
        String password = binding.editPassword.getText().toString().trim();
        binding.editPassword.setText("");

        if(email.isEmpty()){
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.editEmail.setError("Digite o Email");
            binding.editEmail.requestFocus();
            msgLog(1, "");
        }else{
            if(password.isEmpty()){
                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.editPassword.setError("Digite a senha");
                binding.editPassword.requestFocus();
                msgLog(2, "");
            }else{
                loginAccountFirebase(email, password);
            }
        }
        binding.btnLogin.setClickable(true);
    }

    private void loginAccountFirebase(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        currentUser = mAuth.getCurrentUser();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        String message = (Objects.requireNonNull(task.getException()).getMessage());
                        assert message != null;
                        if (message.equals("The email address is badly formatted.")) {
                            binding.editEmail.setError("Email Inválido");
                            binding.editEmail.requestFocus();
                            msgLog(7, "");
                        } else if(message.equals("The supplied auth credential is incorrect, malformed or has expired.")){
                            binding.editEmail.setError("Email ou senha Incorretos");
                            binding.editEmail.requestFocus();
                            msgLog(6, "");
                        } else{
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
        msg[3] = "Login feito com sucesso!";
        msg[4] = "Ocorreu um erro...\nTente Novamente";
        msg[5] = "Senha com menos de 8 dígitos!";
        msg[6] = "Email ou Senha Incorretos.";
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