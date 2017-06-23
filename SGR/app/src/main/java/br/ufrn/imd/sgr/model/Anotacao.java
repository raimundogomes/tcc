package br.ufrn.imd.sgr.model;

/**
 * Created by netou on 10/11/2016.
 */

public class Anotacao{
    private Long id;
    private Integer dia;
    private String titulo;
    private String descricao;
    // getters e setters
    @Override
    public String toString() {
        return "Dia " + dia + " - " + titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
