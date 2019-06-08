package com.example.listadecompras;

public class ModelCompra {
    private String idCompra;
    private String nomeCompra;
    private Double valor;
    private String data;
    private String idUsuario;
    private String idLista;

    public ModelCompra(String _idCompra, Double _valor, String _data, String _idUsuario, String _idLista, String _nomeCompra) {
        this.idCompra = _idCompra;
        this.valor = _valor;
        this.data = _data;
        this.idUsuario = _idUsuario;
        this.idLista = _idLista;
        this.nomeCompra = _nomeCompra;
    }

    public ModelCompra() {

    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
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

    public String getNomeCompra() {
        return nomeCompra;
    }

    public void setNomeCompra(String nomeCompra) {
        this.nomeCompra = nomeCompra;
    }
}
