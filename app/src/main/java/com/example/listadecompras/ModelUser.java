package com.example.listadecompras;

public class ModelUser {
    private String nome;
    private String email;
    private String senha;
    private String id;


    ModelUser(String _nome, String _email, String _senha, String _id) {
        this.nome = _nome;
        this.email = _email;
        this.senha = _senha;
        this.id = _id;
    }

    ModelUser(String _nome, String _email, String _senha) {
        this.nome = _nome;
        this.email = _email;
        this.senha = _senha;
    }


    public ModelUser() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
