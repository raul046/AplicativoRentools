package com.example.rentoolstcc;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AtualizarProduto extends AppCompatActivity {
    String nomeImagem;
    Uri imgFotoUri; // objeto do nome da foto
    ImageView imgFoto;// objeto da foto
    FirebaseFirestore conexaoBD = FirebaseFirestore.getInstance(); // FIRESTORE
    private StorageReference conexaobd = FirebaseStorage.getInstance().getReference();
    AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhe);
        int codigoProduto = getIntent().getIntExtra("codigoProduto", -1);
        nomeImagem = getIntent().getStringExtra("NomeImage");


        // ImageView imgVoltar = findViewById(R.id.imgVoltar);
        Button imgApagar = findViewById(R.id.btnApagarProduto);
        imgFoto = findViewById(R.id.imgDetalhe);

        EditText tVCodigo = findViewById(R.id.edtCodigoProdutoAtualizar);
        EditText tVNomeProduto = findViewById(R.id.edtNomeProdutoAtualizar);
        EditText tVPreco = findViewById(R.id.edtPrecoProdutoAtualizar);
        //radiogrupos

        RadioGroup rgGrupo = findViewById(R.id.rgGrupoP);
        RadioButton rbPneumatica = findViewById(R.id.rbPneumaticaAtualizarP);
        RadioButton rbEletrica = findViewById(R.id.rbEletricaAtualizarP);
        RadioButton rbMecanica = findViewById(R.id.rbMecanicaAtualizarP);

        Button btAtualizarP = findViewById(R.id.btnAtualizarDados);

        conexaoBD.collection("produtos")
                .document(codigoProduto + "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Produto p = document.toObject(Produto.class);
                                tVCodigo.setText("" + p.getCodigo());
                                tVNomeProduto.setText("" + p.getNome());

                                if (rbEletrica.getText().toString().equals(p.getCategoria())) {
                                    rbEletrica.setChecked(true);
                                }
                                if (rbMecanica.getText().toString().equals(p.getCategoria())) {
                                    rbMecanica.setChecked(true);
                                }
                                if (rbPneumatica.getText().toString().equals(p.getCategoria())) {
                                    rbPneumatica.setChecked(true);
                                }

                                tVPreco.setText(""+p.getPreco());

                                final long ONE_MEGABYTE = 1024 * 1024;
                                conexaobd.child("/fotoProdutos/" + p.getFoto()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        imgFoto.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                    }
                                });
                            } else {
                                Toast.makeText(AtualizarProduto.this, "Erro ao buscar", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AtualizarProduto.this, "Erro ao buscar", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AtualizarProduto.this, "Erro ao buscar", Toast.LENGTH_SHORT).show();
                    }
                });

        btAtualizarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemRadioGroupSelecionado = rgGrupo.getCheckedRadioButtonId();
                String opcao;
                if(imgFotoUri != null){
                    nomeImagem = imgFotoUri.getLastPathSegment();
                    //enviar arquivo de foto
                    UploadTask enviarImagem = conexaobd.child("fotoProdutos/" + nomeImagem).putFile(imgFotoUri);
                    //metodo de enviar e ver se deu certo ou não
                    enviarImagem.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AtualizarProduto.this, "Imagem Atualizada", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            exception.printStackTrace();
                            Toast.makeText(AtualizarProduto.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                RadioButton rbCategoriaSelecionada = findViewById(itemRadioGroupSelecionado);
                opcao = rbCategoriaSelecionada.getText().toString();
                Produto produto = new Produto(Integer.parseInt(tVCodigo.getText().toString()),
                        tVNomeProduto.getText().toString(),
                        opcao,
                        Double.parseDouble(tVPreco.getText().toString()), nomeImagem);


                conexaoBD.collection("produtos")
                        .document(codigoProduto + "")
                        .set(produto)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AtualizarProduto.this, "Produto Atualizado Com SUCESSO!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AtualizarProduto.this, "ERRO AO ATUALIZAR Produto", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AtualizarProduto.this, "ERRO CONECTAR Produto", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });





        //Evento do botão "Apagar"
        imgApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AtualizarProduto.this);
                alert.setTitle("Apagar produto");
                alert.setMessage("Deseja mesmo remover o produto?");

                //define um botão como positivo
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        conexaoBD.collection("produtos")
                                .document(codigoProduto + "") //Indicando o código do documento
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AtualizarProduto.this, "Apagado com sucesso", Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(AtualizarProduto.this, "Erro ao apagar produto", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AtualizarProduto.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                //define um botão como negativo
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        alerta.cancel();
                    }
                });

                //Criar um objeto da classe AlertDialog
                alerta = alert.create();
                //Exibir
                alerta.show();
            }
        });

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Intent.ACTION_GET_CONTENT);
                it.setType("image/*");
                resultadoIntent.launch(it);
            }
        });



    }
    //Configurando o método de retorno que será acionado assim que a imagem for escolhida
    ActivityResultLauncher<Intent> resultadoIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        //Teste para verificar se há algo retornando da câmera, ou seja, se há foto
        if (result.getResultCode() == RESULT_OK) {
            //Recuperando os dados enviados pela galeria
            imgFotoUri = result.getData().getData();

            imgFoto.setImageURI(null);

            //Colocar a imagem dentro do ImageView
            imgFoto.setImageURI(imgFotoUri);

            Log.d("CONFERE", ">>>>>>>>>>>>>>>> " + imgFotoUri.getPath());
            Log.d("CONFERE", ">>>>>>>>>>>>>>>> " + imgFotoUri.getEncodedPath());
            Log.d("CONFERE", ">>>>>>>>>>>>>>>> " + imgFotoUri.getScheme());


        }
    });
}