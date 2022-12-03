package com.example.rentoolstcc;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

public class CarrinhoViewHolder extends  RecyclerView.ViewHolder {
    TextView produtoItem, categoriaItem, precoItem;
    ImageView imgDetalheProduto;

    public CarrinhoViewHolder(@NonNull View itemView){
        super(itemView);
        produtoItem = itemView.findViewById(R.id.tVNomeProdutoItemA);
        categoriaItem = itemView.findViewById(R.id.tVCategoriaItem);
        precoItem = itemView.findViewById(R.id.tVPrecoItem);
        imgDetalheProduto = itemView.findViewById(R.id.imgDetalheProduto);


    }

}
