package br.ufrn.imd.sgr.model;


public enum MetodoAmostra {
    TESTE_SENSIBILIDADE(1, "Sensibilidade"), CULTURA(2,"Cultura");

    private int codigo;
    private String descricao;

    MetodoAmostra(int codigo, String descricao) {
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
