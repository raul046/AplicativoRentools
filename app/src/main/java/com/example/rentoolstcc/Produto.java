package com.example.rentoolstcc;

public class Produto {
    //Atributos da classe
    private int codigo;
    private String nome;
    private String categoria;
    private double preco;
    private String foto;


    //Construtores da classe Produto
    public Produto(int codigo, String nome, String categoria, double preco,String foto) {
        this.codigo = codigo;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.foto = foto;

    }
    //Construtor vazio
    public Produto() {  }

    //Getters e Setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}


