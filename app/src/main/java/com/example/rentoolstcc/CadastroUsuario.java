package com.example.rentoolstcc;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastroUsuario extends AppCompatActivity {

    private EditText edtCadastroNome, edtCadastroCpf, edtCadastroEmail, edtCadastroSenha;
    private FirebaseAuth mAuth; // Firebase Authentication
    FirebaseFirestore conexao = FirebaseFirestore.getInstance(); // Firebase Cloud Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        EditText edtCadastroNome = findViewById(R.id.edtCadastroNome);
        EditText edtCadastroCpf = findViewById(R.id.edtCadastroCpf);
        EditText edtCadastroEmail = findViewById(R.id.edtCadastroEmail);
        EditText edtCadastroSenha = findViewById(R.id.edtCadastroSenha);
        Button btnCadastrarUsuario = findViewById(R.id.btnCadastrarUsuario);
        mAuth = FirebaseAuth.getInstance(); // Authentication

        btnCadastrarUsuario.setOnClickListener(new View.OnClickListener() { // EVENTO PARA CADASTRAR UM NOVO USUÁRIO
            @Override
            public void onClick(View v) {
                UsuarioModel usuario = new UsuarioModel(edtCadastroNome.getText().toString(),
                        edtCadastroCpf.getText().toString(),
                        edtCadastroEmail.getText().toString(),
                        edtCadastroSenha.getText().toString());

                String nome = edtCadastroNome.getText().toString();
                String cpf = edtCadastroCpf.getText().toString();
                String email = edtCadastroEmail.getText().toString();
                String senha = edtCadastroSenha.getText().toString();

                if (edtCadastroNome.getText().toString().isEmpty() ||
                        edtCadastroCpf.getText().toString().isEmpty() ||
                        edtCadastroEmail.getText().toString().isEmpty() ||
                        edtCadastroSenha.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroUsuario.this, "Preencha todos com campos!", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                conexao.collection("usuarios")
                                        .document(email)
                                        .set(usuario)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(CadastroUsuario.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                                                } else {
                                                    try {
                                                        throw task.getException();
                                                    }catch(FirebaseAuthWeakPasswordException e){
                                                        edtCadastroSenha.setError("Coloque uma senha com no mínimo 6 caracteres!");
                                                       /* Toast.makeText(CadastroUsuario.this, "Coloque uma senha com no mínimo 6 caracteres!", Toast.LENGTH_LONG).show();*/
                                                    }catch(FirebaseAuthInvalidCredentialsException e){
                                                        Toast.makeText(CadastroUsuario.this, "Digite um e-mail válido!", Toast.LENGTH_LONG).show();
                                                    }catch(FirebaseAuthUserCollisionException e){
                                                        Toast.makeText(CadastroUsuario.this, "E-mail já cadastrado!", Toast.LENGTH_LONG).show();
                                                    }catch(FirebaseNetworkException e){
                                                        Toast.makeText(CadastroUsuario.this, "Sem conexão com a Internet!", Toast.LENGTH_LONG).show();
                                                    }
                                                    catch(Exception e){
                                                        Toast.makeText(CadastroUsuario.this, "Erro ao cadastrar", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CadastroUsuario.this, "Erro de conexão, tente novamente", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                Intent it = new Intent(CadastroUsuario.this, MainActivity.class);
                                startActivity(it);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

    }

}