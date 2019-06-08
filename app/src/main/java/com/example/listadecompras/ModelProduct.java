package com.example.listadecompras;

public class ModelProduct {
    private String produtoNome;
    private String idProduto;
    private String idUsuario;
    private Double produtoValor;

    ModelProduct(String _produtoNome, String _idProduto, String _idUsuario) {
        this.produtoNome = _produtoNome;
        this.idProduto = _idProduto;
        this.idUsuario = _idUsuario;
        this.produtoValor = 0.0;
    }

    ModelProduct() {

    }

    String getProdutoNome() {
        return produtoNome;
    }

    void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    String getIdProduto() {
        return idProduto;
    }

    void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    Double getProdutoValor() {
        return produtoValor;
    }

    void setProdutoValor(Double produtoValor) {
        this.produtoValor = produtoValor;
    }
}
