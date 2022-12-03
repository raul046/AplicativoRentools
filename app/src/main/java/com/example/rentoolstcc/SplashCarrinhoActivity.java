package com.example.rentoolstcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SplashCarrinhoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_carrinho);

        Intent it = getIntent(); // RECEBER VALORES DA INTENT CARRINHO
        String data = it.getStringExtra("data");
        String devolucao = it.getStringExtra("devolucao");
        double totalCarrinho = it.getDoubleExtra("totalCarrinho", 0);

        String horario = it.getStringExtra("horario");

        TextView tvDataEscolhida = findViewById(R.id.tvDataEscolhida); // DATA DE RETIRADA DOS PRODUTOS
        tvDataEscolhida.setText("Data Retirada: " + data); // RECEBER A DATA ACTIVITY CARRINHO

        TextView tvDataDevolucao = findViewById(R.id.tvDataDevolucao); // DATA DE DEVOLUÇÃO DOS PRODUTOS
        tvDataDevolucao.setText("Data Devolução: " + devolucao); // RECEBER A DATA ACTIVITY DOS PRODUTOS

        TextView tvHorarioEscolhido = findViewById(R.id.tvHorarioEscolhido);
        tvHorarioEscolhido.setText("Horário Retirada: " + horario); // RECEBER O HORÁRIO ACTIVITY CARRINHO

        TextView tcarrinho  = findViewById(R.id.tvTotalCarrinho);
        tcarrinho.setText("Valor Total Do Pedido : " + totalCarrinho + "R$");

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                }catch (Exception e){

                }
            }
        }; thread.start();
    }
}