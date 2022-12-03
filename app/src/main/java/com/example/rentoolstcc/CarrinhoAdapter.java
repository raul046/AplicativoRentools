package com.example.rentoolstcc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoViewHolder> {
    List<CarrinhoModel> listaCarrinho;
    Context context;
    float valorTotal;


    public CarrinhoAdapter(List<CarrinhoModel> listaCarrinho, Context context)  {

        this.listaCarrinho = listaCarrinho;
        this.context = context;
    }

    @NonNull
    @Override
    public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Chamando o layout_item.xml para definir como modelo a ser usado
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        return new CarrinhoViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, int position) {
        CarrinhoViewHolder CarrinhoVH = (CarrinhoViewHolder) holder;

        CarrinhoModel clique = listaCarrinho.get(position);





    }

    @Override
    public int getItemCount() {
        return listaCarrinho.size();
    }


}
