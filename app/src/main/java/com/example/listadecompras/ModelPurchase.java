package com.example.listadecompras;

public class ModelPurchase {
    private String idCompra;
    private String nomeCompra;
    private Double valor;
    private String data;
    private String idUsuario;
    private String idLista;

    ModelPurchase(String _idCompra, Double _valor, String _data, String _idUsuario, String _idLista, String _nomeCompra) {
        this.idCompra = _idCompra;
        this.valor = _valor;
        this.data = _data;
        this.idUsuario = _idUsuario;
        this.idLista = _idLista;
        this.nomeCompra = _nomeCompra;
    }

    ModelPurchase() {

    }

    public String getIdCompra() {
        return idCompra;
    }

    void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    Double getValor() {
        return valor;
    }

    void setValor(Double valor) {
        this.valor = valor;
    }

    String getData() {
        return data;
    }

    void setData(String data) {
        this.data = data;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdLista() {
        return idLista;
    }

    public void setIdLista(String idLista) {
        this.idLista = idLista;
    }

    String getNomeCompra() {
        return nomeCompra;
    }

    void setNomeCompra(String nomeCompra) {
        this.nomeCompra = nomeCompra;
    }
}
