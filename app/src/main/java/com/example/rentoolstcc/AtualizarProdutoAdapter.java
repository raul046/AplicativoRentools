package com.example.rentoolstcc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AtualizarProdutoAdapter extends RecyclerView.Adapter<AtualizarProdutoViewholder> {
    List<Produto> listaAtualizarProduto;
    Context context;
    private StorageReference conexaobd = FirebaseStorage.getInstance().getReference();


    public AtualizarProdutoAdapter(List<Produto> listaAtualizarProduto, Context context) {
        this.listaAtualizarProduto= listaAtualizarProduto;
        this.context = context;

    }
    @NonNull
    @Override
    public AtualizarProdutoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Chamando o layout_item.xml para definir como modelo a ser usado

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_atualizar, parent, false);
        return new AtualizarProdutoViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtualizarProdutoViewholder holder, int position) {
        AtualizarProdutoViewholder atualizarprodutoVH = (AtualizarProdutoViewholder) holder;
        atualizarprodutoVH.produtoItem.setText(listaAtualizarProduto.get(position).getNome());
        atualizarprodutoVH.precoItem.setText("R$ " + listaAtualizarProduto.get(position).getPreco());
        Produto produtoClique = listaAtualizarProduto.get(position);

//IMAGEM
        final long ONE_MEGABYTE = 1024 * 1024;
        conexaobd.child("/fotoProdutos/" + listaAtualizarProduto.get(position).getFoto()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                atualizarprodutoVH.imgFotoItem.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });


        atualizarprodutoVH.imgEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(atualizarprodutoVH.itemView.getContext(), AtualizarProduto.class);
                //Adicionando um valor para ser passado como informação para a tela a ser aberta
                it.putExtra("codigoProduto", produtoClique.getCodigo());
                it.putExtra("NomeImage",produtoClique.getFoto());
                //Requisitando que a tela "ProdutoDetalhe" seja aberta
                atualizarprodutoVH.itemView.getContext().startActivity(it);
            }
            });

        }
    public int getItemCount() {
        return listaAtualizarProduto.size();
    }
}
