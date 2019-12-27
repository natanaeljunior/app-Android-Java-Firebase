package br.com.apppedido.Model;

import java.io.Serializable;

public class Cliente implements Serializable {

    private String id;
    private String TipoCliente;
    private String nomeRazao;
    private String apelidoNomeFantasia;
    private String telefone1;
    private String telefone2;
    private String email;
    Endereco endereco;
    private String caminhoFoto;


    public Cliente() {
    }

    public Cliente(String id, String tipoCliente, String nomeRazao, String apelidoNomeFantasia, String telefone1, String telefone2, String email, Endereco endereco, String caminhoFoto) {
        this.id = id;
        this.TipoCliente = tipoCliente;
        this.nomeRazao = nomeRazao;
        this.apelidoNomeFantasia = apelidoNomeFantasia;
        this.telefone1 = telefone1;
        this.telefone2 = telefone2;
        this.email = email;
        this.endereco = endereco;
        this.caminhoFoto = caminhoFoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoCliente() {
        return TipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        TipoCliente = tipoCliente;
    }

    public String getNomeRazao() {
        return nomeRazao;
    }

    public void setNomeRazao(String nomeRazao) {
        this.nomeRazao = nomeRazao;
    }

    public String getApelidoNomeFantasia() {
        return apelidoNomeFantasia;
    }

    public void setApelidoNomeFantasia(String apelidoNomeFantasia) {
        this.apelidoNomeFantasia = apelidoNomeFantasia;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
