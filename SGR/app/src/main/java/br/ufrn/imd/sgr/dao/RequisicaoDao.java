package br.ufrn.imd.sgr.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.sgr.model.Laboratorio;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;

/**
 *
 * Created by neto on 07/11/2016.
 */

public interface RequisicaoDao {

    Requisicao insert(Requisicao requisicao);

    public void delete(long idRequisicao) ;

    public List<Requisicao> listar();

    public void cancelar(Requisicao requisicao);

    public List<Requisicao> consultarPorSituacao(int idSituacao);

}
