package br.ufrn.imd.sgr.TO;

import java.io.Serializable;
import java.util.List;

import br.ufrn.imd.sgr.model.Requisicao;

/**
 * Created by neto on 16/07/2017.
 */

public class RequisicoesAtualizadasTO implements Serializable {

    private List<Requisicao> listaRequisicoes;

    public List<Requisicao> getListaRequisicoes() {
        return listaRequisicoes;
    }

    public void setListaRequisicoes(List<Requisicao> listaRequisicoes) {
        this.listaRequisicoes = listaRequisicoes;
    }
}
