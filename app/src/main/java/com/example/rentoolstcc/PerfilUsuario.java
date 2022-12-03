package com.example.rentoolstcc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PerfilUsuario extends AppCompatActivity {

    FirebaseFirestore conexao = FirebaseFirestore.getInstance(); // Firebase Firestore
    String usuarioID;

    private TextView emailUsuario;
    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        BottomNavigationView bottomNavigationView = findViewById(R.id.menuNav); // MENU PRINCIPAL (HOME-CARRINHO-PERFIL)
        bottomNavigationView.setSelectedItemId(R.id.perfil);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.carrinho:
                        startActivity(new Intent(getApplicationContext(), Carrinho.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.perfil:
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        Componentes();

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PerfilUsuario.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() { // RECUPERAR EMAIL DO USUÁRIO
        super.onStart();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference documentReference = conexao.collection("usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    emailUsuario.setText(email); // recuperar email cadastrado e exibir no textview
                }
            }
        });
    }

    private void Componentes() {
        emailUsuario = findViewById(R.id.emailUsuario);
        btnSair = findViewById(R.id.btnLogout);

        // cardview página meu perfil
        CardView cardEditarDados = findViewById(R.id.cardEditarDados);
        CardView cardSuporte = findViewById(R.id.cardSuporte);
        CardView cardFaq = findViewById(R.id.cardFaq);
        CardView cardExcluirDados = findViewById(R.id.cardExcluirDados);

        // cardview página editar dados
        cardEditarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PerfilUsuario.this, EditarDadosUsuario.class));
                finish();
            }
        });

        // cardview página suporte
        cardSuporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PerfilUsuario.this, Suporte.class));
                finish();
            }
        });

        // cardview página FAQ
        cardFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PerfilUsuario.this, Faq.class));
                finish();
            }
        });
    }
}