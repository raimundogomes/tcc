package br.ufrn.imd.sgr.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by thiago on 29/05/16.
 */
public class Amostra implements Serializable {

    private SituacaoAmostra situacaoAmostra = SituacaoAmostra.EM_ANALISE;

    private Date dataColeta = new Date();

    private MetodoAmostra metodoAmostra = MetodoAmostra.CULTURA;

    public SituacaoAmostra getSituacaoAmostra() {
        return situacaoAmostra;
    }

    public void setSituacaoAmostra(SituacaoAmostra situacaoAmostra) {
        this.situacaoAmostra = situacaoAmostra;
    }

    public Date getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(Date dataColeta) {
        this.dataColeta = dataColeta;
    }

    public MetodoAmostra getMetodoAmostra() {
        return metodoAmostra;
    }

    public void setMetodoAmostra(MetodoAmostra metodoAmostra) {
        this.metodoAmostra = metodoAmostra;
    }
}
