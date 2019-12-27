package br.com.apppedido.Model;

import java.util.ArrayList;
import java.util.Date;

public class Pedido {
    private String id;
    private String nome;
    private ArrayList<Produto> list_produto;
    private Cliente cliente;
    private double valorPedido;
    private Date data;
    private String imgPedido;
    private String status;

    public Pedido(String id, String nome, ArrayList<Produto> list_produto, Cliente cliente, double valorPedido, Date data, String imgPedido, String status) {
        this.id = id;
        this.nome = nome;
        this.list_produto = list_produto;
        this.cliente = cliente;
        this.valorPedido = valorPedido;
        this.data = data;
        this.imgPedido = imgPedido;
        this.status = status;
    }

    public String getImgPedido() {
        return imgPedido;
    }

    public void setImgPedido(String imgPedido) {
        this.imgPedido = imgPedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Pedido() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Produto> getList_produto() {
        return list_produto;
    }

    public void setList_produto(ArrayList<Produto> list_produto) {
        this.list_produto = list_produto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getValorPedido() {
        return valorPedido;
    }

    public void setValorPedido(double valorPedido) {
        this.valorPedido = valorPedido;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
