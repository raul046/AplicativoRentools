package com.example.rentoolstcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarDadosUsuario extends AppCompatActivity {
    private ImageView imagemVoltar;
    private FirebaseAuth mAuth;
    FirebaseFirestore conexao = FirebaseFirestore.getInstance(); //Conexão com firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_dados);

        EditText edAtualizarNome = findViewById(R.id.edAtualizarNome);
        EditText edAtualizarSenha = findViewById(R.id.edAtualizaSenha);
        EditText edAtualizarCPF = findViewById(R.id.edAtualizaCPF);
        Button btnAtualizar = findViewById(R.id.btnAtualizar);

        //Recuperar o email que foi salvo na tela de login
        SharedPreferences ler = getSharedPreferences("dados", MODE_PRIVATE);
        String email = ler.getString("email", "");

        //Busca os dados do usuário e exibe nos campos da tela
        conexao.collection("usuarios")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                UsuarioModel u = document.toObject(UsuarioModel.class);
                                Log.d("TESTE", " >>>>>>>>>>>>>>> " + u.getNome());
                                Log.d("TESTE", " >>>>>>>>>>>>>>> " + u.getSenha());
                                Log.d("TESTE", " >>>>>>>>>>>>>>> " + u.getCpf());

                                edAtualizarNome.setText(u.getNome());
                                edAtualizarSenha.setText(u.getSenha());
                                edAtualizarCPF.setText(u.getCpf());

                            } else {
                                Toast.makeText(EditarDadosUsuario.this, "Erro ao buscar", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditarDadosUsuario.this, "Erro ao buscar", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditarDadosUsuario.this, "Erro ao buscar", Toast.LENGTH_SHORT).show();
                    }
                });

        mAuth = FirebaseAuth.getInstance();

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioModel usuario = new UsuarioModel(edAtualizarNome.getText().toString(), edAtualizarCPF.getText().toString(), email, edAtualizarSenha.getText().toString());

                if (edAtualizarNome.getText().toString().isEmpty() ||
                        edAtualizarSenha.getText().toString().isEmpty() ||
                        edAtualizarCPF.getText().toString().isEmpty()) {
                    Toast.makeText(EditarDadosUsuario.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(email.toString(), edAtualizarSenha.getText().toString());
                    conexao.collection("usuarios")
                            .document(email)
                            .set(usuario)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(edAtualizarSenha.getText().toString());
                                        Toast.makeText(EditarDadosUsuario.this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditarDadosUsuario.this, "Erro ao atualizar dados", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditarDadosUsuario.this, "Erro ao conectar usuário.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        imagemVoltar = findViewById(R.id.imagemVoltar);
        imagemVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(EditarDadosUsuario.this, PerfilUsuario.class);
                startActivity(it);
                finish();
            }
        });

    }
}
