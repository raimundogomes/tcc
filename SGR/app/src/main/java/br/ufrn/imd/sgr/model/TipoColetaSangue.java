package br.ufrn.imd.sgr.model;


public enum TipoColetaSangue {

    VEIA (0, "Veia periférica"),
    ARTERIA   (1, "Arteria"),
    CATETER_UMBILICAL (2, "Cateter umbilical"),
    CATETER_CENTRAL (3, "Cateter central");

    private int codigo;

    private String descricao;

    TipoColetaSangue(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
