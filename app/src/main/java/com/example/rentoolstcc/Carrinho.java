package com.example.rentoolstcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Carrinho extends AppCompatActivity {
    FirebaseFirestore conexao = FirebaseFirestore.getInstance(); // Firebase Cloud Firestore
    private Button btnFinalizar;
    private ProgressBar progressBar;
    private EditText edHorarioPedido;
    private TextView edDataRetirada, edDataDevolucao;
    private ImageView btcarrinho;
    Calendar dataPedido, dataDevolucao; // CALENDÁRIO
    NotificationManagerCompat gerenciador; // NOTIFICAÇÃO
    List<Produto> listaCarrinho = new ArrayList<>();
    ProdutoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        btcarrinho = findViewById(R.id.buttonL);

        SharedPreferences ler = getSharedPreferences("carrinho", MODE_PRIVATE);

        if (ler.getString("carrinho", "{}").equals("{}")) {
            Toast.makeText(this, "O carrinho está vazio", Toast.LENGTH_SHORT).show();
        } else {
            Gson gson = new Gson();

            Type tipoLista = new TypeToken<List<Produto>>() {
            }.getType();

            //Criando uma lista para armazenar objetos do tipo "Produto"
            listaCarrinho = gson.fromJson(ler.getString("carrinho", "{}"), tipoLista);

            RecyclerView recyclerTela = findViewById(R.id.listCarrinho);

            //Passando a lista para o Adapter personalizado
            adapter = new ProdutoAdapter(listaCarrinho, Carrinho.this);

            //Configuração de um gestor de layout
            recyclerTela.setLayoutManager(new LinearLayoutManager(Carrinho.this));

            //Passando o adapter para o RecyclerView
            recyclerTela.setAdapter(adapter);

        }

        btcarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ler.getString("carrinho", "{}").equals("{}")) {
                    Toast.makeText(Carrinho.this, "O carrinho está vazio", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor gravar = getSharedPreferences("carrinho", MODE_PRIVATE).edit();
                    gravar.putString("carrinho", "{}");
                    gravar.apply();
                    listaCarrinho.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        criarCanalNotificacao(); // MÉTODO DO CANAL DE NOTIFICAÇÕES
        gerenciador = NotificationManagerCompat.from(Carrinho.this);

        progressBar = findViewById(R.id.progressBarHorizontal);
        edDataRetirada = findViewById(R.id.edDataRetirada);
        edDataDevolucao = findViewById(R.id.edDataDevolucao);
        edHorarioPedido = findViewById(R.id.edHorarioPedido);
        ImageButton imgBtnDataPedido = findViewById(R.id.imgBtnDataPedido); // imagem button para abrir calendário.
        ImageButton imgBtnDataDevolucao = findViewById(R.id.imgBtnDataDevolucao); // imagem button para abrir calendário.

        imgBtnDataPedido.setOnClickListener(view -> { // EVENTO PARA RETIRADA DOS PRODUTOS
            Calendar calendario = Calendar.getInstance();
            int ano = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            //Cria o calendário com a data atual já selecionada
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Carrinho.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            //Quando a data for alterada para alguma outra além da atual, o código abaixo será executado
                            dataPedido = new GregorianCalendar(year, month, dayOfMonth);

                            //Verificar se a data escolhida não é uma data maior do que o dia atual
                            if (dataPedido.getTimeInMillis() < calendario.getTimeInMillis()) {
                                Toast.makeText(Carrinho.this, "Data inválida", Toast.LENGTH_SHORT).show();
                                edDataRetirada.setText("");
                            } else {
                                //Escrever a data no EditText
                                edDataRetirada.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }
                    },
                    ano, mes, dia);
            //Exibe o calendário
            datePickerDialog.show();
        });

        imgBtnDataDevolucao.setOnClickListener(view -> { // EVENTO PARA DEVOLUÇÃO DOS PRODUTOS
            Calendar calendario = Calendar.getInstance();
            int ano = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            //Cria o calendário com a data atual já selecionada
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Carrinho.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            //Quando a data for alterada para alguma outra além da atual, o código abaixo será executado
                            dataDevolucao = new GregorianCalendar(year, month, dayOfMonth);

                            //Verificar se a data escolhida não é uma data maior do que o dia atual
                            if (dataDevolucao.getTimeInMillis() < calendario.getTimeInMillis()) {

                                Toast.makeText(Carrinho.this, "Data inválida", Toast.LENGTH_SHORT).show();
                                edDataDevolucao.setText("");
                            } else {
                                //Escrever a data no EditText
                                edDataDevolucao.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }
                    },
                    ano, mes, dia);
            //Exibe o calendário
            datePickerDialog.show();
        });

        btnFinalizar = findViewById(R.id.btnFinalizar); // BOTÃO FINALIZAR COMPRA
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemPedidoModel itemPedido = new ItemPedidoModel
                        (edDataRetirada.getText().toString(),
                        edDataDevolucao.getText().toString(),
                        edHorarioPedido.getText().toString()); // ITENS ALUGADOS

                String data = edDataRetirada.getText().toString();
                String horario = edHorarioPedido.getText().toString();

                if (data.isEmpty() || horario.isEmpty()) {
                    Toast.makeText(Carrinho.this, "Escolha a data e o horário !", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    conexao.collection("pedidos")
                            .document()
                            .set(itemPedido)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Carrinho.this, "Pedido Confirmado !", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Carrinho.this, "Erro ao confirmar pedido.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Carrinho.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            });


                    NotificationCompat.Builder notif1 = notificacaoBase("RENTOOLS", "Pedido confirmado ! Obrigado por alugar na Rentools, volte sempre !");
                    gerenciador.notify(1, notif1.build());

                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            double totalCarrinho = 0;
                            for (int i = 0; i < listaCarrinho.size(); i++) {
                                totalCarrinho += listaCarrinho.get(i).getPreco();
                            }

                            Intent it = new Intent(Carrinho.this, SplashCarrinhoActivity.class);

                            it.putExtra("totalCarrinho", totalCarrinho);

                            String data = edDataRetirada.getText().toString();
                            it.putExtra("data", data);

                            String devolucao = edDataDevolucao.getText().toString();
                            it.putExtra("devolucao", devolucao);

                            String horario = edHorarioPedido.getText().toString();
                            it.putExtra("horario", horario);

                            startActivity(it);
                            finish();
                        }
                    }, 1300);

                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.menuNav); // MENU PRINCIPAL (HOME-CARRINHO-PERFIL)

        bottomNavigationView.setSelectedItemId(R.id.carrinho);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.carrinho:
                        return true;

                    case R.id.perfil:
                        startActivity(new Intent(getApplicationContext(), PerfilUsuario.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private NotificationCompat.Builder notificacaoBase(String titulo, String mensagem) { // NOTIFICAÇÃO
        //criar uma Intent que irá direcionar para um activity  (tela) ao tocar na notificação
        Intent abrirTela = new Intent(Carrinho.this, MainActivity.class);
        //transformar a Intent em PendingIntent a qual irá deixar a Intent como pendente, ou seja, aguardando o toque
        PendingIntent pending = PendingIntent.getActivity(
                Carrinho.this, // Contexto
                0, // código de requisição 0 padrão
                abrirTela, // intent que será transformada em pending intent
                PendingIntent.FLAG_CANCEL_CURRENT // modo de operação (atualiza a notificação)
        );

        //config da notificação base
        //no construtor ele pede o contexto e o id do canal (1000)
        NotificationCompat.Builder base = new NotificationCompat.Builder(Carrinho.this, "1000")
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setAutoCancel(true) //fecha notificação após clicar
                .setContentIntent(pending) // o que irá abrir após tocar ?
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.icone_notificacao);
        return base;
    }
    //método para criar canal de notificação, O canal é obrigatório
    //cado o Android seja da versão 8 ou mais recente.

    private void criarCanalNotificacao() {
        //testar qual a versão do Android que está no dispositivo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //o canal de notificações precisa de um nome, descrição e um ID;
            CharSequence nomeCanal = "Canal do aplicativo teste";
            String descricao = "Esse canal é utilizado para exibir notificações do app";
            String idCanal = "1000"; // pode ser qualquer texto ou valor

            //criar o canal de notificação
            NotificationChannel canal = new NotificationChannel(idCanal, nomeCanal, NotificationManager.IMPORTANCE_DEFAULT);
            canal.setDescription(descricao); //atribui a descrição do canal

            //enviar o canal de notificação para que o sistema do Android possa reconhecer
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(canal);

        }
    }
}