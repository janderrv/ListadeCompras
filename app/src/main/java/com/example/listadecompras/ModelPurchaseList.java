package com.example.listadecompras;

public class ModelPurchaseList {
    private String idUsuario;
    private String idProduto;
    private String nomeLista;
    private String idLista;

    ModelPurchaseList(String _idUsuario, String _idProduto, String _nomeLista, String _idLista) {
        this.nomeLista = _nomeLista;
        this.idLista = _idLista;
        this.idUsuario = _idUsuario;
        this.idProduto = _idProduto;
    }

    public ModelPurchaseList(String _idUsuario, String _idProduto, String _nomeLista) {
        this.idUsuario = _idUsuario;
        this.idProduto = _idProduto;
        this.nomeLista = _nomeLista;
    }

    ModelPurchaseList() {

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

    String getNomeLista() {
        return nomeLista;
    }

    void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public String getIdLista() {
        return idLista;
    }

    public void setIdLista(String idLista) {
        this.idLista = idLista;
    }
}
