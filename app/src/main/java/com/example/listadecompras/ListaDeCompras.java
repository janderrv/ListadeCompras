package com.example.listadecompras;

public class ListaDeCompras {
    private Usuario usuario;
    private Produto produto;
    private String nomeLista;
    private String idLista;

    public ListaDeCompras(Usuario _usuario, Produto _produto, String _nomeLista, String _idLista) {
        this.usuario = _usuario;
        this.produto = _produto;
        this.nomeLista = _nomeLista;
        this.idLista = _idLista;
    }

    public ListaDeCompras(Usuario _usuario, Produto _produto, String _nomeLista) {
        this.usuario = _usuario;
        this.produto = _produto;
        this.nomeLista = _nomeLista;
    }

    public ListaDeCompras() {

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Produto getProduto() {
        return produto;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public String getIdLista() {
        return idLista;
    }
}
