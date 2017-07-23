package br.ufrn.imd.sgr.service.impl;

import android.content.Context;
import android.util.Log;

import java.util.List;

import br.ufrn.imd.sgr.dao.ExameDao;
import br.ufrn.imd.sgr.dao.RequisicaoDao;
import br.ufrn.imd.sgr.dao.impl.ExameDaoImpl;
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.service.ExameService;
import br.ufrn.imd.sgr.utils.Constantes;


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

    @Override
    public void atualizarExames(List<Exame> exames) {
        for (Exame e: exames) {
            exameDao.update(e);
            Log.d(Constantes.SGR, ExameServiceImpl.class.getCanonicalName() + " atualizarExames" + e.toString());
        }
    }


}
