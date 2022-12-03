package com.example.rentoolstcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Suporte extends AppCompatActivity {
    private ImageView imagemVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suporte);

        imagemVoltar = findViewById(R.id.imagemVoltar);
        imagemVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Suporte.this, PerfilUsuario.class);
                startActivity(it);
                finish();
            }
        });
    }
}