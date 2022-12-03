package com.example.rentoolstcc;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore conexaoDB = FirebaseFirestore.getInstance();
    private CardView cardEletrica, cardPneumatica, cardMecanica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardEletrica = findViewById(R.id.cardEletrica);
        cardPneumatica = findViewById(R.id.cardPneumatica);
        cardMecanica = findViewById(R.id.cardMecanica);

        BottomNavigationView bottomNavigationView = findViewById(R.id.menuNav); // MENU PRINCIPAL (HOME-CARRINHO-PERFIL)
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.perfil:
                        startActivity(new Intent(getApplicationContext(), PerfilUsuario.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.home:
                        return true;

                    case R.id.carrinho:
                        startActivity(new Intent(getApplicationContext(), Carrinho.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        cardEletrica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, TelaListarEletricos.class);
                startActivity(it);
            }
        });
        cardPneumatica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, TelaListarPneumaticos.class);
                startActivity(it);
            }
        });
        cardMecanica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, TelaListarMecanicos.class);
                startActivity(it);
            }
        });

    }
}