package br.ufrn.imd.sgr.model;


public enum TipoMaterial {

    FERIDA_OPERATORIA (0, "Ferida operatória"),
    ABSCESSO   (1, "Abscesso"),
    ULCERA   (2, "Úlcera"),
    FRAGMENTO_TECIDO (3, "Fragmento de tecido"),
    URINA(4, "Urina"),
    SANGUE(5, "Sangue");


    private int codigo;

    private String descricao;

    TipoMaterial(int codigo, String descricao) {
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

    public static TipoMaterial getTipoMaterialPeloCodigo(int codigo){

        for (TipoMaterial tipo: TipoMaterial.values()){
            if(tipo.codigo== codigo){
                return tipo;
            }
        }

        return null;
    }
}
