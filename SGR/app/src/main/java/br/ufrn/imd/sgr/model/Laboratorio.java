package br.ufrn.imd.sgr.model;


public enum Laboratorio {

    MICROBIOLOGIA (0, "Microbiologia", "84 32758431"),
    CITOLOGIA (1, "Citologia", "(84)32758431");

    private int id;

    private String nome;

    private String telefone;

   Laboratorio(int id, String nome, String telefone) {

        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nome;
    }

    public static Laboratorio getLaboratorioById(int codigo){

        for (Laboratorio laboratorio: Laboratorio.values()){
            if(laboratorio.id== codigo){
                return laboratorio;
            }
        }

        return null;
    }
}
