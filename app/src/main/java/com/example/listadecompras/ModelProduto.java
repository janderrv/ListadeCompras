package com.example.listadecompras;

public class ModelProduto {
    private String produtoNome;
    private String idProduto;

    public ModelProduto(String produtoNome, String idProduto) {
        this.produtoNome = produtoNome;
        this.idProduto = idProduto;
    }

    public ModelProduto() {

    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }
}
