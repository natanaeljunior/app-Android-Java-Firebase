package br.com.apppedido.Model;

import java.io.Serializable;

public class Vendedor implements Serializable {
    private String identificador;
    private String imagem;
    private String UID;

    public Vendedor() {
    }

    public Vendedor(String identificador, String imagem, String UID) {
        this.identificador = identificador;
        this.imagem = imagem;
        this.UID = UID;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
