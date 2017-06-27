package br.ufrn.imd.sgr.model;


public enum SituacaoExame {

    COLETADO(1, "Coletado"),

    EM_ANALISE(2, "Em análise"),

    LIBERADO(3, "Liberado"),

    CANCELADO(4, "Cancelado");

    private int codigo;
    private String descricao;

    SituacaoExame(int codigo, String descricao) {
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

    public static SituacaoExame getSituacaoExamePeloCodigo(int codigo){

        for (SituacaoExame situacaoExame: SituacaoExame.values()){
            if(situacaoExame.codigo== codigo){
                return situacaoExame;
            }
        }

        return null;
    }
}
