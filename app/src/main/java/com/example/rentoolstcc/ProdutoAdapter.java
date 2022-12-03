package com.example.rentoolstcc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutosViewHolder> {
    List<Produto> listaProdutos,ListaCarrinho = new ArrayList<>();
    Context context;
    AlertDialog alerta;
    //Classe para salvar dados do carrinho no aparelho do usuário
    SharedPreferences.Editor gravar;
    SharedPreferences ler;
    //Classe Gson para converter a lista do carrinho para String em JSON
    Gson gson = new Gson();


    private StorageReference conexaobd = FirebaseStorage.getInstance().getReference();

    public ProdutoAdapter(List<Produto> listaProdutos, Context context) {
        this.listaProdutos = listaProdutos;
        this.context = context;
        gravar = context.getSharedPreferences("carrinho", Context.MODE_PRIVATE).edit();
        ler = context.getSharedPreferences("carrinho", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ProdutosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Chamando o layout_item.xml para definir como modelo a ser usado

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        return new ProdutosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutosViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProdutosViewHolder produtoVH = (ProdutosViewHolder) holder;

        produtoVH.produtoItem.setText(listaProdutos.get(position).getNome());
        produtoVH.precoItem.setText("R$ " + listaProdutos.get(position).getPreco());

        produtoVH.imgCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Recupera os dados do produto selecionado
                Produto produtoClique = listaProdutos.get(position);

                //Se o carrinho já tiver algum produto
                if(!ler.getString("carrinho", "{}").equals("{}")){
                    Type tipoLista = new TypeToken<List<Produto>>() {}.getType();
                    //Recupera os produtos que já estão no carrinho
                    ListaCarrinho = gson.fromJson(ler.getString("carrinho", "{}"), tipoLista);
                }

                ListaCarrinho.add(produtoClique);

                gravar.putString("carrinho", gson.toJson(ListaCarrinho));
                gravar.apply();

                Toast.makeText(context, "Produto no Carrinho !", Toast.LENGTH_SHORT).show();

            }
        });

        //IMAGEM
        final long ONE_MEGABYTE = 1024 * 1024;
        conexaobd.child("/fotoProdutos/" + listaProdutos.get(position).getFoto()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                produtoVH.imgFotoItem.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }
    public int getItemCount() {
        return listaProdutos.size();
    }
}
