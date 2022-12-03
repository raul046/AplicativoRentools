package com.example.rentoolstcc;

public class UsuarioModel {
    private String nome, cpf, email, senha;

    public UsuarioModel(String nome, String cpf, String email, String senha) { // construtor
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
    }

    public UsuarioModel() { // construtor vazio
    }

    public String getNome() { // getters and setters
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
