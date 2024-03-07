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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.textCad.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });
        binding.textRec.setOnClickListener(v -> {
            startActivity(new Intent(this, RecuperarActivity.class));
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
            //atualizar a interface
        }
    }

    private void validData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        //coletar strings
        String email = binding.editEmail.getText().toString().trim();
        String passw = binding.editPassword.getText().toString().trim();

        if(email.isEmpty()){
            //email vazio
            binding.editEmail.setError("Digite o Email");
            binding.editEmail.requestFocus();
            msgLog(1, "");
            binding.progressBar.setVisibility(View.INVISIBLE);
        }else{
            if(passw.isEmpty()){
                //senha vazia
                binding.editPassword.setError("Digite a senha");
                binding.editPassword.requestFocus();
                msgLog(2, "");
                binding.progressBar.setVisibility(View.INVISIBLE);
            }else{
                //dados validados
                //solicitando criação da conta
                loginAccountFirebase(email, passw);
            }
        }
    }

    private void loginAccountFirebase(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            String message = (Objects.requireNonNull(task.getException()).getMessage());
                            assert message != null;
                            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        //sucesso
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        msgLog(3, "");
                        //redirecionar para Login
                        //startActivity(new Intent(this, LoginActivity.class));
                        new Handler(getMainLooper()).postDelayed(this::finish, 4001);

                    }else{
                        //error
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        String message = (Objects.requireNonNull(task.getException()).getMessage());
                        assert message != null;

                        if(message.equals("The email address is already in use by another account.")){
                            //email ja utilizado
                            binding.editEmail.setError("Email já está em uso");
                            binding.editEmail.requestFocus();
                            msgLog(6, "");

                        } else if (message.equals("The email address is badly formatted.")) {
                            //email invalido
                            binding.editEmail.setError("Email Inválido");
                            binding.editEmail.requestFocus();
                            msgLog(7, "");

                        } else {
                            //outro erro
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