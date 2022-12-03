package com.example.rentoolstcc;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import javax.annotation.Nullable;

public class CadastroProduto extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance(); // AUTHENTICATION
    FirebaseFirestore conexao = FirebaseFirestore.getInstance(); // FIRESTORE

    private EditText edtNome, edtPreco, edtCodigo, edtfoto;
    private RadioGroup rgGrupo;
    private RadioButton rbPneumatica, rbMecanica, rbEletrica;
    private Button btnCadastrarProduto, btnListar;
    ImageView imgLogoutAdmin;

    FirebaseFirestore conexaoBD = FirebaseFirestore.getInstance();//conexao  com o banco
    private StorageReference storageFb; //objeto para representar o banco de dados
    ImageView imgFoto;// objeto da foto
    Uri imgFotoUri; // objeto do nome da foto
    String nomeImagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        imgFoto = findViewById(R.id.imgFotoProduto);
        edtCodigo = findViewById(R.id.edtCodigo);
        edtNome = findViewById(R.id.edtNome);
        edtPreco = findViewById(R.id.edtPreco);
        rgGrupo = findViewById(R.id.rgGrupo);
        rbPneumatica = findViewById(R.id.rbPneumatica);
        rbEletrica = findViewById(R.id.rbEletrica);
        rbMecanica = findViewById(R.id.rbMecanica);
        btnCadastrarProduto = findViewById(R.id.btnCadastrarProduto);
        btnListar = findViewById(R.id.btnListar);
        imgLogoutAdmin = findViewById(R.id.imgLogoutAdmin);
        storageFb = FirebaseStorage.getInstance().getReference(); // Firebase Storage para imagens

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Intent.ACTION_GET_CONTENT);
                it.setType("image/*");
                resultadoIntent.launch(it);
            }
        });

        //EVENTO BOTÃO CADASTRAR EQUIPAMENTOS - ADMINISTRADOR
        btnCadastrarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemRadioGroupSelecionado = rgGrupo.getCheckedRadioButtonId();
                String opcao;
                if (edtCodigo.getText().toString().isEmpty() || edtNome.getText().toString().isEmpty() ||
                        edtPreco.getText().toString().isEmpty() ||
                        rgGrupo.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CadastroProduto.this, "Preencha todos os campos !", Toast.LENGTH_SHORT).show();
                } else {
                    nomeImagem = imgFotoUri.getLastPathSegment();

                    //enviar arquivo de foto
                    UploadTask enviarImagem = storageFb.child("fotoProdutos/" + nomeImagem).putFile(imgFotoUri);
                    //metodo de enviar e ver se deu certo ou não
                    enviarImagem.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CadastroProduto.this, "Imagem cadastrada", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            exception.printStackTrace();
                            Toast.makeText(CadastroProduto.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                        }
                    });

                    RadioButton rbCategoriaSelecionada = findViewById(itemRadioGroupSelecionado);
                    opcao = rbCategoriaSelecionada.getText().toString();
                    Produto produto = new Produto(Integer.parseInt(edtCodigo.getText().toString()),
                            edtNome.getText().toString(),
                            opcao,
                            Double.parseDouble(edtPreco.getText().toString()), nomeImagem);

                    conexao.collection("produtos")
                            .document(edtCodigo.getText().toString())
                            .set(produto)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CadastroProduto.this, "Produto cadastrado com sucesso !", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(CadastroProduto.this, "Erro ao cadastrar produto", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CadastroProduto.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            });
                }
            }
        });

        //EVENTO BOTÃO LISTAR EQUIPAMENTOS - ADMINISTRADOR
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(CadastroProduto.this, ListaProdutos.class);
                startActivity(it);
            }
        });

        imgLogoutAdmin.setOnClickListener(new View.OnClickListener() { // LOGOUT CONTA ADMINISTRADOR
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CadastroProduto.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //Configurando o método de retorno que será acionado assim que a imagem for escolhida
    ActivityResultLauncher<Intent> resultadoIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        //Teste para verificar se há algo retornando da câmera, ou seja, se há foto
        if (result.getResultCode() == RESULT_OK) {
            //Recuperando os dados enviados pela galeria
            imgFotoUri = result.getData().getData();

            //Colocar a imagem dentro do ImageView
            imgFoto.setImageURI(imgFotoUri);

            Log.d("CONFERE", ">>>>>>>>>>>>>>>> " + imgFotoUri.getPath());
            Log.d("CONFERE", ">>>>>>>>>>>>>>>> " + imgFotoUri.getEncodedPath());
            Log.d("CONFERE", ">>>>>>>>>>>>>>>> " + imgFotoUri.getScheme());
        }
    });
}

