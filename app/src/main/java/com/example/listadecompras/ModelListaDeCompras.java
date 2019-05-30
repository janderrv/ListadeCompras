package com.example.listadecompras;

public class ModelListaDeCompras {
    private String idUsuario;
    private String idProduto;
    private String nomeLista;
    private String idLista;

    public ModelListaDeCompras(String _idUsuario, String _idProduto, String _nomeLista, String _idLista) {
        this.nomeLista = _nomeLista;
        this.idLista = _idLista;
        this.idUsuario = _idUsuario;
        this.idProduto = _idProduto;
    }

    public ModelListaDeCompras(String _idUsuario, String _idProduto, String _nomeLista) {
        this.idUsuario = _idUsuario;
        this.idProduto = _idProduto;
        this.nomeLista = _nomeLista;
    }

    public ModelListaDeCompras() {

    }


    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public void setIdLista(String idLista) {
        this.idLista = idLista;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public String getIdLista() {
        return idLista;
    }
}
