package br.ufrn.imd.sgr.model;

import java.io.Serializable;
import java.util.Date;

public class Exame implements Serializable {

    private long id;

    private TipoExame tipoExame;

    private TipoColeta tipoColeta;

    private Date dataColeta;

    private SituacaoExame situacaoExame = SituacaoExame.COLETADO;

    private TipoMaterial tipoMaterial;

    private ResultadoExame resultadoExame = ResultadoExame.ANALISE_NAO_REALIZADA;

    private String resultadoCompleto ;
    private long idRequisicao;

    public String getResultadoCompleto() {
        return resultadoCompleto;
    }

    public TipoColeta getTipoColeta() {
        return tipoColeta;
    }

    public void setTipoColeta(TipoColeta tipoColeta) {
        this.tipoColeta = tipoColeta;
    }

    public Date getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(Date dataColeta) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TipoExame getTipoExame() {
        return tipoExame;
    }

    public void setTipoExame(TipoExame tipoExame) {
        this.tipoExame = tipoExame;
    }

    public long getIdRequisicao() {
        return idRequisicao;
    }

    public long setIdRequisicao(long idRequisicao) {
        return this.idRequisicao = idRequisicao;
    }

    public SituacaoExame getSituacaoExame() {
        return situacaoExame;
    }

    public void setSituacaoExame(SituacaoExame situacaoExame) {
        this.situacaoExame = situacaoExame;
    }
}
