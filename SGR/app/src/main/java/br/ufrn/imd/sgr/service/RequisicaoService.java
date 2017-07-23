package br.ufrn.imd.sgr.service;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.List;

import br.ufrn.imd.sgr.activities.novaRequisicao.NovaRequisicaoActivity;
import br.ufrn.imd.sgr.activities.RequisicoesActivity;
import br.ufrn.imd.sgr.model.Requisicao;

public interface RequisicaoService {


    void cancelarRequisicaoServico(final Requisicao requisicao, final Context applicationContext);

    void desconectar(final Context applicationContext);

    Requisicao persistirRequisicao(Requisicao requisicao);

    boolean validarRequisicao(final Requisicao requisicao);

    Requisicao salvarRequisicao(final Requisicao requisicao,final NovaRequisicaoActivity novaRequisicaoActivity);

    Requisicao salvarRequisicaoSemInternet(final Requisicao requisicao,final NovaRequisicaoActivity novaRequisicaoActivity) ;

    Requisicao atualizarRequisicoes(final RequisicoesActivity novaRequisicaoActivity);

    List<Requisicao> consultarRequisicoesSolicitadas();

    void excluir(long id);

    List<Requisicao> listar();
}
