package com.example.listadecompras;

public class ModelProduto {
    private String produtoNome;
    private String idProduto;
    private String idUsuario;

    public ModelProduto(String _produtoNome, String _idProduto, String _idUsuario) {
        this.produtoNome = _produtoNome;
        this.idProduto = _idProduto;
        this.idUsuario = _idUsuario;
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

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
