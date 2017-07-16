package br.ufrn.imd.sgr.dao;

import java.util.List;

import br.ufrn.imd.sgr.model.Exame;

public interface ExameDao {

    Exame consultarPeloId(long id);

    List<Exame> consultarPeloIdRequisicao(long id);

    Exame insert(Exame exame);

    void update(Exame exame);

    void delete(long idRequisicao);
}
