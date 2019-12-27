package br.com.apppedido.Model;

import java.io.Serializable;

public class Endereco implements Serializable {
    private String cep;
    private String cidadeMunicipio;
    private String endereco;
    private String UF;

    public Endereco() {
    }

    public String getUF() {
        return UF;
    }

    public void setUF(String UF) {
        this.UF = UF;
    }

    public Endereco(String cep, String cidadeMunicipio, String endereco) {
        this.cep = cep;
        this.cidadeMunicipio = cidadeMunicipio;
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidadeMunicipio() {
        return cidadeMunicipio;
    }

    public void setCidadeMunicipio(String cidadeMunicipio) {
        this.cidadeMunicipio = cidadeMunicipio;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
