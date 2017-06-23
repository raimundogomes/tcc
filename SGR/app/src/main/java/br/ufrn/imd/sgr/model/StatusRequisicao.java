package br.ufrn.imd.sgr.model;


public enum StatusRequisicao {
    SOLICITADA (0, "Solicitada"),
    RECEBIDA_PELO_LABORATORIO(1, "Recebida pelo laboratório"),
    LAUDO_PARCIAL (2, "Laudo parcial"),
    CANCELADA (3, "Cancelada"),
    LAUDO_DEFINITIVO(4, "Realizado");

    private int codigo;

    private String descricao;

    StatusRequisicao(int codigo, String descricao) {
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

    public static StatusRequisicao getStatusRequisicaoByCodigo(int codigo){

     for (StatusRequisicao status: StatusRequisicao.values()){
         if(status.codigo== codigo){
             return status;
         }
     }

     return SOLICITADA;
 }
}
