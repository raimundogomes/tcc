package br.ufrn.imd.sgr.model;

/**
 * Created by Neto on 02/06/2016.
 */
public class Email {

    private String[] enderecos;

    private String assunto;

    private String conteudo;

    public Email(String[] endereco, String assunto, String conteudo) {
        this.enderecos = endereco;
        this.assunto = assunto;
        this.conteudo = conteudo;
    }

    public String[] getEnderecos() {
        return enderecos;
    }

    public void setEndereco( String[] endereco) {
        this.enderecos = endereco;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
