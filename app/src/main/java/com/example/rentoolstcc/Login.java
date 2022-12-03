package com.example.rentoolstcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText edtEmail, edtSenha;
    private Button btnEntrar;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

        TextView btn = findViewById(R.id.btnTelaCadastro);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, CadastroUsuario.class));
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(Login.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SharedPreferences.Editor gravar = getSharedPreferences("dados", MODE_PRIVATE).edit(); // salvando dados no dispositivo da pessoa,podendo abrir em qualquer tela
                                gravar.putString("email", email); // criar campo com o nome email e colocar em email
                                gravar.apply();
                                Toast.makeText(Login.this, "Login efetuado com sucesso!", Toast.LENGTH_LONG).show();

                                if (email.contains("admin@gmail.com") && senha.contains("123456")) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(Login.this, CadastroProduto.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 1500);

                                } else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 1500);
                                }
                            } else {
                                Snackbar.make(findViewById(android.R.id.content), "Erro no login, verifique seus dados", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}