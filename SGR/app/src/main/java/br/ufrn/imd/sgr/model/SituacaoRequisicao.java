package br.ufrn.imd.sgr.model;


public enum SituacaoRequisicao {
    SOLICITADA (0, "Solicitada"),
    RECEBIDA (1, "Cancelada"),
    CANCELADA (2, "Cancelada"),
    FINALIZADA(3, "Finalizada"),
    REJEITADA(4, "Rejeitada");

    private int codigo;

    private String descricao;

    SituacaoRequisicao(int codigo, String descricao) {
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

    public static SituacaoRequisicao getStatusRequisicaoByCodigo(int codigo){

     for (SituacaoRequisicao status: SituacaoRequisicao.values()){
         if(status.codigo== codigo){
             return status;
         }
     }

     return SOLICITADA;
 }
}
