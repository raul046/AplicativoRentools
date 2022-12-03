package com.example.rentoolstcc;

public class CarrinhoModel {
    //Atributos da classe
    private String nome;
    private  int preco;


    public CarrinhoModel(String nome, int preco) {
        this.nome = nome;
        this.preco = preco;
    }


    public CarrinhoModel() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }
}
