package br.ufrn.imd.sgr.model;


public enum TipoColeta {

    VEIA (0, "Veia periférica"),
    ARTERIA   (1, "Arteria"),
    CATETER_UMBILICAL (2, "Cateter umbilical"),
    CATETER_CENTRAL (3, "Cateter central"),
    JATO_MEDIO (4, "Jato médio"),
    SVD   (5, "SVD"),
    SONDA_ALIVIO   (6, "Sonda alívio"),
    PUNCAO_SUBPUBICA (7, "Punção subpúbica"),
    SACO_COLETOR (8, "Saco coletor"),
    SWAB (9, "SWAB"),
    ASPIRADO_AGULHA (10, "SWAB");

    private int codigo;

    private String descricao;

    TipoColeta(int codigo, String descricao) {
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

    public static TipoColeta getTipoColetaPeloCodigo(int codigo){

        for (TipoColeta tipoColeta: TipoColeta.values()){
            if(tipoColeta.codigo== codigo){
                return tipoColeta;
            }
        }

        return null;
    }
}
