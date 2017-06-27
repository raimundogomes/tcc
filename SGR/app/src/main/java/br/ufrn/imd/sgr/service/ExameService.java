package br.ufrn.imd.sgr.service;

import java.util.List;

import br.ufrn.imd.sgr.model.Exame;

/**
 * Created by netou on 26/06/2017.
 */

public interface ExameService {

    List<Exame> consultarExames(long idRequisicao);
}
