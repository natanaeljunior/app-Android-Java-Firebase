package br.com.apppedido.Model;

import java.util.Date;

public class Pagamentos {
    String id;
    Cliente cliente;
    Date vencimento;
    String valor;
    Pedido pedido;
    String status;


    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Pagamentos() {
    }

    public Pagamentos(String id, Cliente cliente, Date vencimento, String valor, Pedido pedido, String status) {
        this.id = id;
        this.cliente = cliente;
        this.vencimento = vencimento;
        this.valor = valor;
        this.pedido = pedido;
        this.status = status;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }



    public Date getVencimento() {
        return vencimento;
    }

    public String getValor() {
        return valor;
    }

}
