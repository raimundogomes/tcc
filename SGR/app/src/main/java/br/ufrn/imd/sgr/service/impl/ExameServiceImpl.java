package br.ufrn.imd.sgr.service.impl;

import android.content.Context;

import java.util.List;

import br.ufrn.imd.sgr.dao.ExameDao;
import br.ufrn.imd.sgr.dao.impl.ExameDaoImpl;
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.service.ExameService;


public class ExameServiceImpl implements ExameService {

    private ExameDao exameDao;

    public ExameServiceImpl(Context contexto){
        exameDao = new ExameDaoImpl(contexto);
    }

    public List<Exame> consultarExames(long idRequisicao){

        return exameDao.consultarPeloIdRequisicao(idRequisicao);
    }

    @Override
    public Exame insert(Exame exame) {
        return exameDao.insert(exame);
    }

    @Override
    public void delete(long idRequisicao) {
        exameDao.delete(idRequisicao);
    }


}
