package com.example.rentoolstcc;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProdutosViewHolder extends RecyclerView.ViewHolder {
    TextView produtoItem,  precoItem;
    Button detalheProduto;
    ImageView imgFotoItem,imgCarrinho;

    public ProdutosViewHolder(@NonNull View itemView) {
        super(itemView);
        //Vincular o ID para cada componente
        //codigoItem = itemView.findViewById(R.id.tVCodigoItem);
        produtoItem = itemView.findViewById(R.id.tVNomeProdutoItem);
       //categoriaItem = itemView.findViewById(R.id.tVCategoriaItem);
        precoItem = itemView.findViewById(R.id.tVPrecoItem);
       // detalheProduto = itemView.findViewById(R.id.btnDetalheProduto);
        imgCarrinho = itemView.findViewById(R.id.imgAddCarrinho);
        imgFotoItem = itemView.findViewById(R.id.imageFoto);
    }
}
