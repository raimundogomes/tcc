package br.ufrn.imd.sgr.model;


public enum TipoExame {

    SANGUE (0, "Hemocultura"),
    URINA   (1, "Urocultura"),
    SECRECAO (2, "Secrecao");

    private int codigo;

    private String descricao;

    TipoExame(int codigo, String descricao) {
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

    public static TipoExame getTipoExamePeloCodigo(int codigo){

        for (TipoExame tipoExame: TipoExame.values()){
            if(tipoExame.codigo== codigo){
                return tipoExame;
            }
        }

        return null;
    }
}
