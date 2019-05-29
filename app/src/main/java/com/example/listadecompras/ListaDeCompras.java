package com.example.listadecompras;

public class ListaDeCompras {

    private String nome;

    public ListaDeCompras() {
    }

    public ListaDeCompras(String _nome) {
        this.nome = _nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
