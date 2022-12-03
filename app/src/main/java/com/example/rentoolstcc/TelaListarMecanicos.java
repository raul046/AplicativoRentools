package com.example.rentoolstcc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TelaListarMecanicos extends AppCompatActivity {
    FirebaseFirestore conexao = FirebaseFirestore.getInstance(); // FIRESTORE
    private ImageView imgVoltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_listar_mecanicos);
        //Criando uma lista para armazenar objetos do tipo "Produto"
        List<Produto> listaProdutos = new ArrayList<>();

        conexao.collection("produtos")
                .whereEqualTo("categoria","Ferramentas Mecânicas")
                .get() //Utilizando o método get() para recuperar todas os documentos da coleção
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //Se o comando for executado com sucesso, então
                        if (task.isSuccessful()) {
                            //Repetição para recuperar todos os documentos da coleção
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Convertendo o documento para um objeto do tipo "Produto"
                                Produto p = document.toObject(Produto.class);
                                //Adicionando o produto na lista
                                listaProdutos.add(p);
                            }

                            //Após encerrar a repetição e adicionar todos os produtos na lista
                            //fazemos a configuração do RecyclerView e adicionamos a lista para ser exibida nele
                            //Criando um objeto do RecyclerView da tela
                            RecyclerView recyclerTela = findViewById(R.id.idRecyclerMecanicas);

                            //Passando a lista para o Adapter personalizado
                            ProdutoAdapter adapter = new ProdutoAdapter(listaProdutos, TelaListarMecanicos.this);

                            //Configuração de um gestor de layout
                            recyclerTela.setLayoutManager(new LinearLayoutManager(TelaListarMecanicos.this));
                            //Passando o adapter para o RecyclerView
                            recyclerTela.setAdapter(adapter);

                        } else {
                            //Exibindo informação caso apresente um erro na conexão
                            Toast.makeText(TelaListarMecanicos.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Caso a busca encontre algum erro, esse método é chamado
                        Toast.makeText(TelaListarMecanicos.this, "Erro ao buscar", Toast.LENGTH_SHORT).show();
                    }
                });
























        imgVoltar = findViewById(R.id.imgVoltar);
        imgVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TelaListarMecanicos.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        });
    }
}