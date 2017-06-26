package br.ufrn.imd.sgr.model;

import java.io.Serializable;
import java.util.Date;

public class Exame implements Serializable {

    private TipoExame tipoExame;

    private TipoColeta tipoColeta;

    private String dataColeta;

    private TipoMaterial tipoMaterial;

    private ResultadoExame resultadoExame = ResultadoExame.ANALISE_NAO_REALIZADA;

    private String resultadoCompleto ;

    public String getResultadoCompleto() {
        return resultadoCompleto;
    }

    public TipoColeta getTipoColeta() {
        return tipoColeta;
    }

    public void setTipoColeta(TipoColeta tipoColeta) {
        this.tipoColeta = tipoColeta;
    }

    public String getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(String dataColeta) {
        this.dataColeta = dataColeta;
    }

    public TipoMaterial getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(TipoMaterial tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public void setResultadoCompleto(String resultadoCompleto) {
        this.resultadoCompleto = resultadoCompleto;
    }

    public ResultadoExame getResultadoExame() {
        return resultadoExame;
    }

    public void setResultadoExame(ResultadoExame resultadoExame) {
        this.resultadoExame = resultadoExame;
    }

    public Exame(){}

    public Exame(TipoExame tipo) {
        tipoExame = tipo;
    }


    @Override
    public String toString() {
        return tipoExame.getDescricao();
    }

    public String getDescricao(){
        return tipoExame.getDescricao();
    }

    public String getResultado(){

        if(resultadoExame==null){
            return "";
        }

        return resultadoExame.getDescricao();

    }

    public TipoExame getTipoExame() {
        return tipoExame;
    }

    public void setTipoExame(TipoExame tipoExame) {
        this.tipoExame = tipoExame;
    }
}
