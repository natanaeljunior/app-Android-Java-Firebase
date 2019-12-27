package br.com.apppedido.Model;

import java.io.Serializable;

public class Produto implements Serializable {
    private String id;
    private String imgProduto;
    private String nome;
    private String preco;
    private String codBarra;
    private int qtd;


    public Produto(String id, String imgProduto, String nome, String preco, String codBarra, int qtd) {
        this.id =  id;
        this.imgProduto = imgProduto;
        this.nome = nome;
        this.preco = preco;
        this.codBarra = codBarra;
        this.qtd = qtd;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public Produto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }
    public String getImgProduto() {
        return imgProduto;
    }

    public void setImgProduto(String imgProduto) {
        this.imgProduto = imgProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

}
