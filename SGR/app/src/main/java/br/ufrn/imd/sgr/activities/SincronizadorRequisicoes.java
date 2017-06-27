package br.ufrn.imd.sgr.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.utils.DetectaConexao;

/**
 * Created by netou on 24/06/2017.
 */

public class SincronizadorRequisicoes {

    public void sicronizarRequisicoes(List<Requisicao> requisicoes, Context contexto) {

        DetectaConexao detectaConexao = new DetectaConexao(contexto);
        if(detectaConexao.existeConexao()){

            exibirMensagemSicronizacao(contexto);
        }
        else{
            Toast toast = Toast.makeText(contexto, DetectaConexao.FALHA_CONEXAO,           Toast.LENGTH_LONG);
            toast.show();
        }

    }

    private void exibirMensagemSicronizacao(Context contexto) {

        final ProgressDialog dialog = new ProgressDialog(contexto);
        dialog.setTitle("Sincronizando as requisições...");
        dialog.setMessage("Aguarde, por favor.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();

        long delayInMillis = 2000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delayInMillis);

    }
}
