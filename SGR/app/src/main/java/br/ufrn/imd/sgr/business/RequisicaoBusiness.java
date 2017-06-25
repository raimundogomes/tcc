package br.ufrn.imd.sgr.business;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufrn.imd.sgr.RequisicoesActivity;
import br.ufrn.imd.sgr.activities.PesquisarPacienteActivity;
import br.ufrn.imd.sgr.dao.RequisicaoDao;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.model.StatusRequisicao;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.DetectaConexao;


public class RequisicaoBusiness {

    private RequestQueue queue;

    RequisicaoDao requisicaoDao;

    public RequisicaoBusiness(RequisicoesActivity requisicoesActivity) {
        queue = Volley.newRequestQueue(requisicoesActivity);

        requisicaoDao = new RequisicaoDao(requisicoesActivity);
    }

    public void cancelarRequisicaoServico(Requisicao requisicao, final Context applicationContext) {

        String url = Constantes.URL_REQUISICAO + "cancelarRequisicao";

        final JSONObject jsonBody;
        try {
            jsonBody = new JSONObject("{\"numeroRequisicao\":\"" + requisicao.getNumeroFormatado() +  "\"}");

            Log.d("Teste", jsonBody.get("numeroRequisicao").toString());

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response
                    .Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Teste", response.toString());

                    Toast.makeText(applicationContext,
                            "Requisição cancelada com sucesso.",
                            Toast.LENGTH_SHORT).show();
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Teste", error.toString());
                    Toast.makeText(applicationContext,
                            "Não foi possível estabelecer conexão para cancelar a requisição.",
                            Toast.LENGTH_LONG).show();
                }
            });

            queue.add(jsObjRequest);

            requisicaoDao.cancelar(requisicao);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void desconectar(final Context applicationContext) {
        SharedPreferences sp = applicationContext.getSharedPreferences(Constantes.PREF_NAME, applicationContext.MODE_PRIVATE);

        boolean b = sp.getBoolean(Constantes.CONFIGURACAO_CONECTADO, false);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();

    }


}
