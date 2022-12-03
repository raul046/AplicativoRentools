package com.example.rentoolstcc;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AtualizarProdutoViewholder extends RecyclerView.ViewHolder {
    TextView produtoItem,  precoItem;
    Button detalheProduto;
    ImageView imgFotoItem,imgEditar;

    public AtualizarProdutoViewholder(@NonNull View itemView) {
        super(itemView);

        //Vincular o ID para cada componente
        //codigoItem = itemView.findViewById(R.id.tVCodigoItem);
        produtoItem = itemView.findViewById(R.id.tVNomeProdutoItemA);
        //categoriaItem = itemView.findViewById(R.id.tVCategoriaItem);
        precoItem = itemView.findViewById(R.id.tVPrecoItemA);
        // detalheProduto = itemView.findViewById(R.id.btnDetalheProduto);
        imgEditar = itemView.findViewById(R.id.imgEditar);
        imgFotoItem = itemView.findViewById(R.id.imageFotoA);
    }

}

